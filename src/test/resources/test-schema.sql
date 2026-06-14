-- H2 测试用 Schema（用于 @MybatisTest 切片测试）
-- 表结构与 MySQL 主表一致（列名使用下划线，与 resultMap 匹配）

CREATE TABLE IF NOT EXISTS drone (
    id                BIGINT       AUTO_INCREMENT PRIMARY KEY,
    name              VARCHAR(100) NOT NULL,
    model             VARCHAR(100) NOT NULL,
    serial_number     VARCHAR(100),
    manufacturer      VARCHAR(100),
    type              VARCHAR(50),
    weight            DOUBLE,
    max_speed         DOUBLE,
    max_altitude      DOUBLE,
    endurance         DOUBLE,
    range_distance    DOUBLE,
    max_payload       DOUBLE,
    camera_type       VARCHAR(200),
    battery_capacity  INT,
    gps_accuracy      DOUBLE,
    operating_temperature VARCHAR(50),
    waterproof_level  VARCHAR(20),
    status            TINYINT      DEFAULT 1,
    description       VARCHAR(500),
    purchase_date     TIMESTAMP,
    ai_generated      TINYINT      DEFAULT 0,
    create_time       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    update_time       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    deleted           TINYINT      DEFAULT 0
);
