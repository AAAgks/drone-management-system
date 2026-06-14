-- ============================================================
-- 无人机信息管理系统 — 数据库初始化脚本
-- 适用数据库：MySQL 5.7+ / 8.0+
-- 使用方式：mysql -u root -p < docs/init-mysql.sql
-- ============================================================

CREATE DATABASE IF NOT EXISTS drone_db DEFAULT CHARSET utf8mb4;
USE drone_db;

-- 删除旧表（如果存在）
DROP TABLE IF EXISTS drone;

-- 无人机信息表
CREATE TABLE drone (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    name              VARCHAR(100)  NOT NULL COMMENT '无人机名称',
    model             VARCHAR(100)  NOT NULL COMMENT '型号',
    serial_number     VARCHAR(100)  DEFAULT NULL COMMENT '序列号（唯一）',
    manufacturer      VARCHAR(100)  DEFAULT NULL COMMENT '制造商',
    type              VARCHAR(50)   DEFAULT NULL COMMENT '类型：多旋翼/固定翼/直升机/垂直起降',
    weight            DOUBLE        DEFAULT NULL COMMENT '重量（kg）',
    max_speed         DOUBLE        DEFAULT NULL COMMENT '最大速度（km/h）',
    max_altitude      DOUBLE        DEFAULT NULL COMMENT '最大高度（m）',
    endurance         DOUBLE        DEFAULT NULL COMMENT '续航（min）',
    range_distance    DOUBLE        DEFAULT NULL COMMENT '最大航程（km）',
    max_payload       DOUBLE        DEFAULT NULL COMMENT '最大载重（kg）',
    camera_type       VARCHAR(200)  DEFAULT NULL COMMENT '相机类型',
    battery_capacity  INT           DEFAULT NULL COMMENT '电池容量（mAh）',
    gps_accuracy      DOUBLE        DEFAULT NULL COMMENT 'GPS精度（cm）',
    operating_temperature VARCHAR(50) DEFAULT NULL COMMENT '工作温度',
    waterproof_level  VARCHAR(20)   DEFAULT NULL COMMENT '防水等级',
    status            TINYINT       DEFAULT 1 COMMENT '状态：1-正常，0-停用',
    description       VARCHAR(500)  DEFAULT NULL COMMENT '描述',
    purchase_date     DATETIME      DEFAULT NULL COMMENT '购入日期',
    ai_generated      TINYINT       DEFAULT 0 COMMENT 'AI生成：1-是，0-否',
    create_time       DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time       DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted           TINYINT       DEFAULT 0 COMMENT '逻辑删除：0-正常，1-已删除',

    UNIQUE KEY uk_serial (serial_number),
    INDEX idx_name (name),
    INDEX idx_model (model),
    INDEX idx_status (status),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='无人机信息表';

-- 测试数据
INSERT INTO drone (name, model, serial_number, manufacturer, type, weight, max_speed, max_altitude, endurance, range_distance, max_payload, camera_type, battery_capacity, gps_accuracy, operating_temperature, waterproof_level, status, description, purchase_date, ai_generated, deleted) VALUES
('大疆 Mavic 3', 'Mavic 3 Pro', 'UAV-20240101-0001', '大疆创新', '多旋翼', 0.92, 75.0, 6000.0, 46.0, 30.0, 0.5, '4K/60fps Hasselblad', 5000, 1.5, '-10℃~40℃', 'IP43', 1, '消费级旗舰航拍无人机', '2024-01-15', 0, 0),
('大疆 Matrice 300 RTK', 'M300 RTK', 'UAV-20240301-0002', '大疆创新', '多旋翼', 6.3, 82.0, 7000.0, 55.0, 15.0, 2.7, 'H20T 四光云台', 12000, 1.0, '-20℃~50℃', 'IP55', 1, '工业级旗舰飞行平台', '2024-03-01', 0, 0),
('极飞 P100 Pro', 'P100 Pro 2024', 'UAV-20240501-0003', '极飞科技', '多旋翼', 48.0, 13.8, 30.0, 18.0, 2.0, 100.0, '多光谱相机', 30000, 2.0, '-5℃~45℃', 'IP65', 1, '农业植保无人机，最大载重100kg', '2024-05-10', 0, 0),
('翼龙-2', 'Wing Loong II', 'UAV-20231201-0004', '中航工业', '固定翼', 1200.0, 370.0, 10000.0, 480.0, 200.0, 480.0, '光电/SAR/电子战吊舱', 80000, 0.3, '-40℃~55℃', 'IP67', 1, '中空长航时察打一体无人机', '2023-12-01', 0, 0),
('道通 EVO Lite+', 'EVO Lite+', 'UAV-20240701-0005', '道通智能', '多旋翼', 0.84, 65.0, 5000.0, 40.0, 25.0, 0.3, '1英寸CMOS 6K', 4500, 1.2, '-5℃~40℃', 'IP43', 0, '已停用的消费级无人机', '2024-07-01', 0, 0);
