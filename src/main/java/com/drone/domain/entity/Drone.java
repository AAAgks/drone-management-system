package com.drone.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 无人机信息实体，对应数据库表 {@code drone}。
 * <p>
 * 每个字段通过 MyBatis 的 {@code map-underscore-to-camel-case} 配置
 * 自动与数据库下划线列名（如 {@code serial_number}）映射。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Drone {

    /** 主键 ID，数据库自增 */
    private Long id;
    /** 无人机名称 */
    private String name;
    /** 无人机型号 */
    private String model;
    /** 序列号，唯一标识 */
    private String serialNumber;
    /** 制造商 / 品牌 */
    private String manufacturer;
    /** 类型：多旋翼、固定翼、直升机、垂直起降 */
    private String type;
    /** 重量（kg） */
    private Double weight;
    /** 最大飞行速度（km/h） */
    private Double maxSpeed;
    /** 最大飞行高度（m） */
    private Double maxAltitude;
    /** 续航时间（min） */
    private Double endurance;
    /** 最大航程（km） */
    private Double rangeDistance;
    /** 最大载重（kg） */
    private Double maxPayload;
    /** 相机类型 / 规格 */
    private String cameraType;
    /** 电池容量（mAh） */
    private Integer batteryCapacity;
    /** GPS 定位精度（cm） */
    private Double gpsAccuracy;
    /** 工作温度范围 */
    private String operatingTemperature;
    /** 防水等级（如 IP43、IP67） */
    private String waterproofLevel;
    /** 状态：1-正常，0-停用 */
    private Integer status;
    /** 描述 / 备注 */
    private String description;
    /** 购入日期 */
    private LocalDateTime purchaseDate;
    /** 是否 AI 生成：1-是，0-否（手动录入） */
    private Integer aiGenerated;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 最后更新时间 */
    private LocalDateTime updateTime;
    /** 逻辑删除标记：0-正常，1-已删除 */
    private Integer deleted;
}
