package com.jinpei.re.db.domain;

import com.jinpei.re.common.ReUtils;
import com.jinpei.re.db.dto.ActivityCreateDTO;
import com.jinpei.re.model.Activity;
import com.jinpei.re.db.dto.ActivitySearchDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 营销配置服务接口
 *
 * @author Mingo.Liu
 * @date 2022-09-29
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;

    /**
     * 创建营销活动
     *
     * @param createDTO 营销活动创建请求
     * @return 营销活动信息
     */
    @Transactional(timeout = 5, rollbackFor = Exception.class)
    public Activity create(ActivityCreateDTO createDTO) {
        ActivityEntity activity = ReUtils.copy(createDTO, new ActivityEntity(), true);
        activityRepository.save(activity);
        return activity.toValueObject();
    }

    /**
     * 删除营销活动
     *
     * @param id 营销活动ID
     */
    @Transactional(timeout = 5, rollbackFor = Exception.class)
    public void delete(Long id) {
        activityRepository.deleteById(id);
    }

    /**
     * 查询营销活动
     *
     * @param searchDTO 查询请求
     * @return 分页查询结果
     */
    @Transactional(timeout = 10, readOnly = true, rollbackFor = Exception.class)
    public Page<Activity> queryPage(ActivitySearchDTO searchDTO) {
        Specification<ActivityEntity> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (null != searchDTO.getLoginType()) {
                predicates.add(builder.equal(root.get("loginType"), searchDTO.getLoginType()));
            }

            if (StringUtils.isNotBlank(searchDTO.getRegion())) {
                predicates.add(builder.equal(root.get("region"), searchDTO.getRegion()));
            }
            if (StringUtils.isNotBlank(searchDTO.getTerminalType())) {
                predicates.add(builder.lessThan(root.get("terminalType"), searchDTO.getTerminalType()));
            }

            query.where(predicates.toArray(new Predicate[0]));
            return query.getRestriction();
        };
        return activityRepository.findAll(specification, searchDTO.toSpringPageRequest())
                .map(ActivityEntity::toValueObject);
    }

    /**
     * 获取所有营销活动
     *
     * @return 所有营销活动列表
     */
    public List<Activity> queryAll() {
        return activityRepository.findAll(Sort.by(Sort.Order.desc("weight"), Sort.Order.desc("id")))
                .stream()
                .map(ActivityEntity::toValueObject)
                .collect(Collectors.toList());
    }
}
