package com.drone.domain.dto;

import lombok.Data;

import javax.validation.constraints.Size;

/**
 * 修改无人机请求 DTO。
 * <p>
 * 所有字段可选——前端只传需要修改的字段，Service 层只覆盖非 null 值。
 */
@Data
public class DroneUpdateRequest {

    private Long id;

    @Size(max = 100, message = "名称最大100个字符")
    private String name;

    @Size(max = 100, message = "型号最大100个字符")
    private String model;

    @Size(max = 100, message = "序列号最大100个字符")
    private String serialNumber;

    @Size(max = 100, message = "制造商最大100个字符")
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
    private String status;
    private String description;
    private String purchaseDate;
}
