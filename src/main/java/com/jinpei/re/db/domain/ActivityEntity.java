package com.jinpei.re.db.domain;

import com.jinpei.re.common.ReUtils;
import com.jinpei.re.model.Activity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 营销活动配置
 *
 * @author Mingo.Liu
 * @date 2022-09-29
 */
@Entity
@Table(name = "t_activity", indexes = {@Index(name = "idx_activity_region", columnList = "region"),
        @Index(name = "uk_activity_name", columnList = "name")})
@Data
public class ActivityEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "bigint not null comment 'ID，主键'")
    private Long id;

    /**
     * 营销规则名称
     */
    @Column(name = "name", nullable = false, updatable = false, unique = true, columnDefinition = "varchar(64) comment '营销规则名称'")
    private String name;

    /**
     * 地区
     */
    @Column(name = "region", columnDefinition = "varchar(64) comment '地区'")
    private String region;

    /**
     * 设备类型, website、box、phone
     */
    @Column(name = "terminal_type", columnDefinition = "varchar(64) comment '设备类型'")
    private String terminalType;

    /**
     * 登陆类型 0(Device login)、1(Email login)、2(Account login)、3(Mobile login)、4(Email binding login)、5(Mobile binding login)
     */
    @Column(name = "login_type", columnDefinition = "tinyint comment '登陆类型'")
    private Integer loginType;

    /**
     * 渠道类型
     */
    @Column(name = "channel_types", columnDefinition = "text comment '渠道类型'")
    @ElementCollection(targetClass = String.class)
    private List<String> channelTypes;

    /**
     * 活动规则权重
     */
    @Column(name = "weight", nullable = false, columnDefinition = "int not null comment '活动规则权重'")
    private Integer weight = 1;

    /**
     * 折扣
     */
    @Column(name = "discount", columnDefinition = "decimal(6,4) not null comment '折扣'")
    private BigDecimal discount = BigDecimal.ONE;

    public Activity toValueObject() {
        return ReUtils.copy(this, new Activity(), true);
    }
}
