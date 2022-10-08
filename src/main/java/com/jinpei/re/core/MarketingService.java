package com.jinpei.re.core;

import com.jinpei.re.core.model.Activity;
import com.jinpei.re.core.model.UserAttribute;
import com.jinpei.re.dto.ActivityCreateDTO;
import com.jinpei.re.dto.ActivityUpdateDTO;

import java.util.List;

/**
 * 营销接口
 *
 * @author Mingo.Liu
 * @date 2022-09-28
 */
public interface MarketingService {
    /**
     * 计算满足用户条件的营销活动
     *
     * @param userAttribute 用户属性
     * @return 满足条件的营销活动列表
     */
    List<Activity> calculate(UserAttribute userAttribute);

    /**
     * 添加营销活动，并更新规则引擎
     *
     * @param createDTO 营销活动添加请求
     * @return 添加成功的营销活动
     */
    Activity add(ActivityCreateDTO createDTO);

    /**
     * 删除营销活动，并更新规则引擎
     *
     * @param id 要删除的营销活动ID
     */
    void delete(Long id);

    /**
     * 更新营销活动，并更新规则引擎
     *
     * @param updateDTO 营销活动更新请求
     * @return 更新成功的营销活动
     */
    Activity update(ActivityUpdateDTO updateDTO);
}
