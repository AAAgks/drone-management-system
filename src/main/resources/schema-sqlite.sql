-- ==================================================================
-- SQLite 建表脚本（spring.profiles=sqlite 时使用）
-- 注意：SQLite 不支持 COMMENT，列注解写在下方注释中
-- ==================================================================
CREATE TABLE IF NOT EXISTS drone (
    id INTEGER PRIMARY KEY AUTOINCREMENT,       -- 主键，自增
    name TEXT NOT NULL,                          -- 无人机名称
    model TEXT NOT NULL,                         -- 无人机型号
    serial_number TEXT,                          -- 序列号
    manufacturer TEXT,                           -- 制造商
    type TEXT,                                   -- 类型
    weight REAL,                                 -- 重量(kg)
    max_speed REAL,                              -- 最大速度(km/h)
    max_altitude REAL,                           -- 最大高度(m)
    endurance REAL,                              -- 续航(min)
    range_distance REAL,                         -- 最大航程(km)
    max_payload REAL,                            -- 最大载重(kg)
    camera_type TEXT,                            -- 相机类型
    battery_capacity INTEGER,                    -- 电池容量(mAh)
    gps_accuracy REAL,                           -- GPS精度(cm)
    operating_temperature TEXT,                  -- 工作温度
    waterproof_level TEXT,                       -- 防水等级
    status TEXT DEFAULT '1',                   -- 状态：1-正常，0-停用
    description TEXT,                            -- 描述
    purchase_date TEXT,                          -- 购入日期
    ai_generated INTEGER DEFAULT 0,              -- 来源
    deleted INTEGER DEFAULT 0,                   -- 逻辑删除
    create_time TEXT DEFAULT (datetime('now')),   -- 创建时间
    update_time TEXT DEFAULT (datetime('now'))    -- 更新时间
);
