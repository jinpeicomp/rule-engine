package com.jinpei.re.dto;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.springframework.data.domain.Sort.Order;

/**
 * 营销活动查询请求
 *
 * @author Mingo.Liu
 * @date 2022-09-29
 */

@Data
@SuppressWarnings("AlibabaPojoNoDefaultValue")
public class ActivitySearchDTO {

    /**
     * 页码，从0开始
     */
    private Integer pageNumber = 0;

    /**
     * 每页大小
     */
    private Integer pageSize = 20;

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

    public PageRequest toSpringPageRequest() {
        return PageRequest.of(pageNumber, pageSize, Sort.by(Order.desc("weight"), Order.desc("id")));
    }
}
