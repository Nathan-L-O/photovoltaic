-- 逆变器
CREATE TABLE `photovoltaic`.`inverter`  (
    `inverter_id` integer NOT NULL AUTO_INCREMENT,
    `inverter_name` varchar(255) NULL DEFAULT NULL,
    `inverter_output_power` varchar(255) NULL DEFAULT NULL COMMENT '输出功率',
    `inverter_lower_limit` varchar(255) NULL DEFAULT NULL COMMENT '电压下限',
    `inverter_up_limit` varchar(255) NULL DEFAULT NULL COMMENT '电压上限',
    `inverter_type` varchar(255) NULL DEFAULT NULL,
    `inverter_json` longtext NULL,
    `inverter_pic` varchar(255) NULL COMMENT '图片地址',
    PRIMARY KEY (`inverter_id`) USING BTREE
)ENGINE = InnoDB CHARACTER SET = utf8 COLLATE =  utf8_general_ci ROW_FORMAT = Dynamic;

-- 项目对于的方案
CREATE TABLE `photovoltaic`.`form`  (
    `form_id` integer NOT NULL AUTO_INCREMENT,
    `form_json` longtext NULL,
    `programme_id` int(0) NULL DEFAULT NULL COMMENT '方案id',
    `demand_capacity` varchar(255) NULL COMMENT '需求发电量',
    `actual_capacity` varchar(255) NULL COMMENT '实际发电量',
    `chose` int(0) NULL DEFAULT NULL COMMENT '是否选用',
    `errmsg` varchar(255) NULL DEFAULT NULL,
    PRIMARY KEY (`form_id`) USING BTREE
)ENGINE = InnoDB CHARACTER SET = utf8 COLLATE =  utf8_general_ci ROW_FORMAT = Dynamic;

-- 项目
CREATE TABLE `photovoltaic`.`programme`  (
    `programme_id` integer NOT NULL AUTO_INCREMENT,
    `programme_name` varchar(255) NULL,
    `user_id` varchar(255) NULL,
    `update_date` datetime NULL,
    `create_date` datetime NULL,
    `isCollection` int NULL COMMENT '收藏',
    `isDelete` int NULL COMMENT '删除',
    `demand_capacity` varchar(255) NULL COMMENT '需求发电量',
    `actual_capacity` varchar(255) NULL COMMENT '实际发电量',
    `programme_state` varchar(255) NULL,
    `programme_purpose` varchar(255)  NULL COMMENT '方案用途',
    `inverter_id` varchar(255)  NULL COMMENT '逆变器id',
    `inverter_num` varchar(255)  NULL COMMENT '逆变器数量',
    PRIMARY KEY (`programme_id`)
)ENGINE = InnoDB CHARACTER SET = utf8 COLLATE =  utf8_general_ci ROW_FORMAT = Dynamic;

-- 电池
CREATE TABLE `photovoltaic`.`battery`  (
    `battery_id` int(0) NOT NULL AUTO_INCREMENT,
    `battery_type` varchar(255) NULL COMMENT '电池分类',
    `battery_name` varchar(255) NULL COMMENT '电池名称',
    `battery_pic` varchar(255) NULL COMMENT '图片地址',
    `battery_json` longtext NULL,
    PRIMARY KEY (`battery_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE =  utf8_general_ci ROW_FORMAT = Dynamic;

-- 用户
CREATE TABLE `user` (
                        `id` bigint NOT NULL,
                        `createTime` datetime DEFAULT NULL,
                        `updateTime` datetime DEFAULT NULL,
                        `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                        `salt` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                        `sessionId` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
                        `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                        `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                        PRIMARY KEY (`id`) USING BTREE,
                        UNIQUE KEY `uk_user_id` (`user_id`) USING BTREE,
                        UNIQUE KEY `uk_user_name` (`username`) USING BTREE,
                        UNIQUE KEY `uk_session_id` (`sessionId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=COMPACT;

-- 用户信息
CREATE TABLE `user_info` (
                             `id` bigint NOT NULL,
                             `createTime` datetime DEFAULT NULL,
                             `updateTime` datetime DEFAULT NULL,
                             `user_info_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                             `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
                             `real_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
                             `sex` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
                             `mobile_phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
                             `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
                             `company` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
                             `nickname` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
                             `ext_info` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
                             `job` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE KEY `uk_user_info_id_user_id` (`user_info_id`,`user_id`) USING BTREE,
                             UNIQUE KEY `uk_user_id` (`user_id`) USING BTREE,
                             UNIQUE KEY `uk_user_info_id` (`user_info_id`) USING BTREE,
                             UNIQUE KEY `uk_major_id_class_id_user_info_id` (`user_info_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=COMPACT;