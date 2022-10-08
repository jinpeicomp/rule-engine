package com.jinpei.re.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 营销活动更新请求
 *
 * @author Mingo.Liu
 * @date 2022-10-08
 */
@Data
public class ActivityUpdateDTO implements Serializable {
    private Long id;

    /**
     * 活动规则权重
     */
    private Integer weight;

    /**
     * 折扣
     */
    private BigDecimal discount;
}
