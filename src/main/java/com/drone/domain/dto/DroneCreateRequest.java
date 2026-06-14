package com.drone.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 新增无人机请求 DTO。
 * <p>
 * 使用 JSR-303 Bean Validation 注解对必填字段和长度进行校验。
 */
@Data
public class DroneCreateRequest {

    @NotBlank(message = "无人机名称不能为空")
    @Size(max = 100, message = "名称最大100个字符")
    private String name;

    @NotBlank(message = "型号不能为空")
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
    /** 状态：前端传中文"正常"/"停用"，Service 层转为数字 */
    private String status;
    private String description;
    /** 购入日期，ISO 格式字符串 */
    private String purchaseDate;
}
