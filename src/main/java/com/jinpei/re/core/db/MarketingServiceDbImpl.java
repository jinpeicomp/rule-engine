package com.jinpei.re.core.db;

import com.jinpei.re.common.ReUtils;
import com.jinpei.re.core.MarketingService;
import com.jinpei.re.core.db.domain.ActivityService;
import com.jinpei.re.core.db.engine.DynamicRuleEngine;
import com.jinpei.re.core.model.Activity;
import com.jinpei.re.core.model.UserAttribute;
import com.jinpei.re.dto.ActivityAnalyzeResponse;
import com.jinpei.re.dto.ActivityCreateDTO;
import com.jinpei.re.dto.ActivitySearchDTO;
import com.jinpei.re.dto.ActivityUpdateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 通过动态规则引擎实现营销接口
 *
 * @author Mingo.Liu
 * @date 2022-10-08
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MarketingServiceDbImpl implements MarketingService {

    @Qualifier("dynamicRuleEngine")
    private final DynamicRuleEngine dynamicRuleEngine;

    private final ActivityService activityService;

    /**
     * 计算满足用户条件的营销活动
     *
     * @param userAttribute 用户属性
     * @return 满足条件的营销活动列表
     */
    @Override
    public List<Activity> calculate(UserAttribute userAttribute) {
        long startTime = System.currentTimeMillis();
        List<Long> activityIds = dynamicRuleEngine.execute(userAttribute);
        log.info("Calculate procedure engine execute spends {}", System.currentTimeMillis() - startTime);
        if (CollectionUtils.isEmpty(activityIds)) {
            return Collections.emptyList();
        }

        long queryStartTime = System.currentTimeMillis();
        List<Activity> activities = activityService.queryByIds(activityIds);
        log.info("Calculate procedure query activity spends {}", System.currentTimeMillis() - queryStartTime);
        log.info("Calculate total spends {}", System.currentTimeMillis() - startTime);

        return activities;
    }

    /**
     * 添加营销活动，并更新规则引擎
     *
     * @param createDTO 营销活动添加请求
     * @return 添加成功的营销活动
     */
    @Override
    @Transactional(timeout = 10, rollbackFor = Exception.class)
    public Activity add(ActivityCreateDTO createDTO) {
        Activity activity = activityService.create(createDTO);
        dynamicRuleEngine.addOrUpdateRule(activity);
        return activity;
    }

    /**
     * 删除营销活动，并更新规则引擎
     *
     * @param id 要删除的营销活动ID
     */
    @Override
    @Transactional(timeout = 10, rollbackFor = Exception.class)
    public void delete(Long id) {
        Activity activity = activityService.delete(id);
        if (null != activity) {
            dynamicRuleEngine.deleteRule(activity.getName());
        }
    }

    /**
     * 更新营销活动，并更新规则引擎
     *
     * @param updateDTO 营销活动更新请求
     * @return 更新成功的营销活动
     */
    @Override
    @Transactional(timeout = 10, rollbackFor = Exception.class)
    public Activity update(ActivityUpdateDTO updateDTO) {
        Activity activity = activityService.update(updateDTO);
        dynamicRuleEngine.addOrUpdateRule(activity);
        return activity;
    }

    /**
     * 查询营销活动
     *
     * @param searchDTO 查询请求
     * @return 分页查询结果
     */
    @Override
    @Transactional(timeout = 10, rollbackFor = Exception.class)
    public Page<Activity> queryPage(ActivitySearchDTO searchDTO) {
        return activityService.queryPage(searchDTO);
    }

    /**
     * 分析待创建的营销活动和已存在的营销活动是否冲突
     *
     * @param createDTO 待创建的营销活动
     * @return 冲突分析结果
     */
    @Override
    public ActivityAnalyzeResponse analyze(ActivityCreateDTO createDTO) {
        long startTime = System.currentTimeMillis();

        List<UserAttribute> userAttributes = getUserAttributes(createDTO);
        if (CollectionUtils.isEmpty(userAttributes)) {
            return ActivityAnalyzeResponse.empty();
        }

        List<Long> conflictActivityIds = detectConflict(userAttributes);
        if (CollectionUtils.isEmpty(conflictActivityIds)) {
            return ActivityAnalyzeResponse.empty();
        }

        ActivityAnalyzeResponse response = buildAnalyzeResponse(createDTO, conflictActivityIds);
        log.info("Analyze total spends {}", System.currentTimeMillis() - startTime);
        return response;
    }

    /**
     * 构造用户属性
     *
     * @param createDTO 待创建的营销活动
     * @return 用户属性列表
     */
    private List<UserAttribute> getUserAttributes(ActivityCreateDTO createDTO) {
        long startTime = System.currentTimeMillis();

        List<String> terminalTypes = ReUtils.ofStringBlank(createDTO.getTerminalType())
                .map(Collections::singletonList)
                .orElse(Arrays.asList("website", "box", "phone"));
        List<Integer> loginTypes = Optional.ofNullable(createDTO.getLoginType())
                .map(Collections::singletonList)
                .orElse(Arrays.asList(0, 1, 2, 3, 4, 5));
        List<String> channelTypes = ReUtils.ofListEmpty(createDTO.getChannelTypes())
                .orElse(activityService.queryChannelType(createDTO.getRegion()));

        List<UserAttribute> userAttributes = new ArrayList<>();
        terminalTypes.forEach(terminalType ->
                loginTypes.forEach(loginType ->
                        channelTypes.forEach(channelType -> {
                            UserAttribute userAttribute = UserAttribute.builder()
                                    .region(createDTO.getRegion())
                                    .terminalType(terminalType)
                                    .loginType(loginType)
                                    .channelType(channelType)
                                    .build();
                            userAttributes.add(userAttribute);
                        })));

        log.info("Analyze procedure get user attributes spends {}", System.currentTimeMillis() - startTime);
        return userAttributes;
    }

    /**
     * 检测冲突
     *
     * @param userAttributes 用户属性列表
     * @return 冲突的营销活动ID
     */
    private List<Long> detectConflict(List<UserAttribute> userAttributes) {
        long startTime = System.currentTimeMillis();
        List<Long> conflictActivityIds = userAttributes.stream()
                .map(dynamicRuleEngine::execute)
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
        log.info("Analyze procedure detect conflict spends {}", System.currentTimeMillis() - startTime);
        return conflictActivityIds;
    }


    /**
     * 构造冲突分析结果
     *
     * @param createDTO           待创建的营销活动
     * @param conflictActivityIds 冲突的营销活动ID列表
     * @return 冲突分析结果
     */
    private ActivityAnalyzeResponse buildAnalyzeResponse(ActivityCreateDTO createDTO, List<Long> conflictActivityIds) {
        long startTime = System.currentTimeMillis();

        List<Activity> conflictActivities = activityService.queryByIds(conflictActivityIds);
        List<Activity> priorActivities = conflictActivities.stream()
                .filter(conflictActivity -> conflictActivity.getWeight() > createDTO.getWeight())
                .collect(Collectors.toList());
        List<Activity> brokenActivities = conflictActivities.stream()
                .filter(conflictActivity -> conflictActivity.getWeight() <= createDTO.getWeight())
                .collect(Collectors.toList());
        ActivityAnalyzeResponse response = ActivityAnalyzeResponse.builder()
                .brokenActivities(brokenActivities)
                .priorActivities(priorActivities)
                .build();

        log.info("Analyze procedure build response spends {}", System.currentTimeMillis() - startTime);
        return response;
    }
}
