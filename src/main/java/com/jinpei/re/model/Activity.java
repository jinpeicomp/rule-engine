package com.jinpei.re.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 营销活动配置值对象
 *
 * @author Mingo.Liu
 * @date 2022-09-29
 */
@Data
public class Activity implements Serializable {
    private Long id;

    /**
     * 营销规则名称
     */
    private String name;

    /**
     * 地区
     */
    private String region;

    /**
     * 设备类型, website、box、phone
     */
    private String terminalType;

    /**
     * 登陆类型 0(Device login)、1(Email login)、2(Account login)、3(Mobile login)、4(Email binding login)、5(Mobile binding login)
     */
    private Integer loginType;

    /**
     * 渠道类型
     */
    private List<String> channelTypes;

    /**
     * 活动规则权重
     */
    private Integer weight;

    /**
     * 折扣
     */
    private BigDecimal discount;
}
