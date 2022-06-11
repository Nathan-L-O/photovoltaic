-- 逆变器
CREATE TABLE `photovoltaic`.`inverter`  (
    `inverter_id` integer NOT NULL AUTO_INCREMENT,
    `inverter_name` varchar(255) NULL DEFAULT NULL,
    `inverter_output_power` varchar(255) NULL DEFAULT NULL COMMENT '输出功率',
    `inverter_price` varchar(255) NULL DEFAULT NULL COMMENT '价格',
    `inverter_lower_limit` varchar(255) NULL DEFAULT NULL COMMENT '电压下限',
    `inverter_up_limit` varchar(255) NULL DEFAULT NULL COMMENT '电压上限',
    `inverter_type` varchar(255) NULL DEFAULT NULL,
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
    `isCollection` int NULL COMMENT '收藏',
    `isDelete` int NULL COMMENT '删除',
    `demand_capacity` varchar(255) NULL COMMENT '需求发电量',
    `actual_capacity` varchar(255) NULL COMMENT '实际发电量',
    `programme_state` varchar(255) NULL,
    PRIMARY KEY (`programme_id`)
)ENGINE = InnoDB CHARACTER SET = utf8 COLLATE =  utf8_general_ci ROW_FORMAT = Dynamic;

-- 电池
CREATE TABLE `photovoltaic`.`battery`  (
    `battery_id` int(0) NOT NULL AUTO_INCREMENT,
    `battery_type` varchar(255) NULL COMMENT '电池分类',
    `battery_brand` varchar(255) NULL COMMENT '电池品牌',
    `battery_name` varchar(255) NULL COMMENT '电池名称',
    `battery_size` varchar(255) NULL COMMENT '电池尺寸',
    `battery_pic` varchar(255) NULL COMMENT '图片地址',
    `battery_voltage` varchar(255) NULL COMMENT '电池电压',
    `battery_electric_current` varchar(255) NULL COMMENT '电池电流',
    PRIMARY KEY (`battery_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE =  utf8_general_ci ROW_FORMAT = Dynamic;