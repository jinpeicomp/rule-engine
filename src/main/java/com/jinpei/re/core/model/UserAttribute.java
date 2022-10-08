package com.jinpei.re.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户属性
 *
 * @author Mingo.Liu
 * @date 2022-09-28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAttribute {
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
    private String channelType;
}