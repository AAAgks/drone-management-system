package com.drone.domain.dto;

import com.drone.domain.entity.Drone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 无人机响应 DTO（Data Transfer Object）。
 * <p>
 * 用于返回给前端的数据格式，将实体中的日期时间转为字符串，并计算状态文本。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DroneDTO {

    private Long id;
    private String name;
    private String model;
    private String serialNumber;
    private String manufacturer;
    private String type;
    private Double weight;
    private Double maxSpeed;
    private Double maxAltitude;
    private Double endurance;
    private Double rangeDistance;
    private Double maxPayload;
    private String cameraType;
    private Integer batteryCapacity;
    private Double gpsAccuracy;
    private String operatingTemperature;
    private String waterproofLevel;
    /** 状态值：1-正常，0-停用 */
    private Integer status;
    /** 状态文本：由 status 计算得出 */
    private String statusText;
    private String description;
    /** 购入日期字符串 */
    private String purchaseDate;
    /** 来源标记：1-AI 生成，0-手动录入 */
    private Integer aiGenerated;
    /** 创建时间字符串 */
    private String createTime;
    /** 更新时间字符串 */
    private String updateTime;

    /**
     * 从数据库实体构建前端 DTO。
     * <p>
     * 将 {@link LocalDateTime} 转为 ISO 字符串，将数字 status 转为中文文本。
     */
    public static DroneDTO fromEntity(Drone drone) {
        return DroneDTO.builder()
                .id(drone.getId())
                .name(drone.getName())
                .model(drone.getModel())
                .serialNumber(drone.getSerialNumber())
                .manufacturer(drone.getManufacturer())
                .type(drone.getType())
                .weight(drone.getWeight())
                .maxSpeed(drone.getMaxSpeed())
                .maxAltitude(drone.getMaxAltitude())
                .endurance(drone.getEndurance())
                .rangeDistance(drone.getRangeDistance())
                .maxPayload(drone.getMaxPayload())
                .cameraType(drone.getCameraType())
                .batteryCapacity(drone.getBatteryCapacity())
                .gpsAccuracy(drone.getGpsAccuracy())
                .operatingTemperature(drone.getOperatingTemperature())
                .waterproofLevel(drone.getWaterproofLevel())
                .status(drone.getStatus())
                .statusText(drone.getStatus() != null && drone.getStatus() == 1 ? "正常" : "停用")
                .description(drone.getDescription())
                .purchaseDate(drone.getPurchaseDate() != null ? drone.getPurchaseDate().toLocalDate().toString() : null)
                .aiGenerated(drone.getAiGenerated())
                .createTime(drone.getCreateTime() != null ? drone.getCreateTime().toString() : null)
                .updateTime(drone.getUpdateTime() != null ? drone.getUpdateTime().toString() : null)
                .build();
    }
}
