package com.jinpei.re.core.db;

import com.jinpei.re.core.MarketingService;
import com.jinpei.re.core.db.domain.ActivityService;
import com.jinpei.re.core.db.engine.DynamicRuleEngine;
import com.jinpei.re.core.model.Activity;
import com.jinpei.re.core.model.UserAttribute;
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

import java.util.Collections;
import java.util.List;

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
        List<Long> activityIds = dynamicRuleEngine.execute(userAttribute);
        if (CollectionUtils.isEmpty(activityIds)) {
            return Collections.emptyList();
        }
        return activityService.queryByIds(activityIds);
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
}
