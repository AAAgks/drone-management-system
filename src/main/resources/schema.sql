-- ==================================================================
-- MySQL 建表脚本（启动时自动执行）
-- ==================================================================
CREATE TABLE IF NOT EXISTS drone (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键，自增',
    name VARCHAR(100) NOT NULL COMMENT '无人机名称',
    model VARCHAR(100) NOT NULL COMMENT '无人机型号',
    serial_number VARCHAR(100) COMMENT '序列号',
    manufacturer VARCHAR(100) COMMENT '制造商/品牌',
    type VARCHAR(50) COMMENT '类型：多旋翼/固定翼/直升机/垂直起降',
    weight DOUBLE COMMENT '重量(kg)',
    max_speed DOUBLE COMMENT '最大速度(km/h)',
    max_altitude DOUBLE COMMENT '最大飞行高度(m)',
    endurance DOUBLE COMMENT '续航时间(min)',
    range_distance DOUBLE COMMENT '最大航程(km)',
    max_payload DOUBLE COMMENT '最大载重(kg)',
    camera_type VARCHAR(100) COMMENT '相机类型/规格',
    battery_capacity INT COMMENT '电池容量(mAh)',
    gps_accuracy DOUBLE COMMENT 'GPS定位精度(cm)',
    operating_temperature VARCHAR(50) COMMENT '工作温度范围',
    waterproof_level VARCHAR(20) COMMENT '防水等级',
    status INT DEFAULT 1 COMMENT '状态：1-正常，0-停用',
    description TEXT COMMENT '描述/备注',
    purchase_date DATE COMMENT '购入日期',
    ai_generated INT DEFAULT 0 COMMENT '来源：1-AI生成，0-手动录入',
    deleted INT DEFAULT 0 COMMENT '逻辑删除：0-正常，1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='无人机信息表';
