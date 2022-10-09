DROP TABLE IF EXISTS `hibernate_sequence`;
CREATE TABLE `hibernate_sequence`
(
    `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
INSERT INTO `hibernate_sequence`(`next_val`)
VALUES (1);

DROP TABLE IF EXISTS `t_activity`;
CREATE TABLE `t_activity`
(
    `id`            bigint        NOT NULL COMMENT 'ID，主键',
    `name`          varchar(64)   NOT NULL COMMENT '营销规则名称',
    `region`        varchar(64) DEFAULT NULL COMMENT '地区',
    `terminal_type` varchar(64) DEFAULT NULL COMMENT '设备类型',
    `login_type`    tinyint     DEFAULT NULL COMMENT '登陆类型',
    `channel_types` text COMMENT '渠道类型',
    `weight`        int           NOT NULL COMMENT '活动规则权重',
    `discount`      decimal(8, 4) NOT NULL COMMENT '折扣',
    PRIMARY KEY (`id`),
    KEY             `idx_activity_region` (`region`),
    KEY             `uk_activity_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;