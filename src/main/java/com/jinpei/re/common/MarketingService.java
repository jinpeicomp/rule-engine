package com.jinpei.re.common;

import com.jinpei.re.model.Activity;
import com.jinpei.re.model.UserAttribute;

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
}
