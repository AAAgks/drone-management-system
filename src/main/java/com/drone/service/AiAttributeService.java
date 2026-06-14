package com.drone.service;

import com.drone.domain.dto.DroneDTO;

/**
 * AI 属性生成服务接口。
 * <p>
 * 根据无人机名称和描述，自动推断等级并在合理范围内生成技术参数。
 */
public interface AiAttributeService {

    /**
     * 根据名称和描述自动生成无人机属性。
     *
     * @param name        无人机名称
     * @param description 产品描述（可选）
     * @return 填充了属性字段的 DTO（id 为 null，需用户确认后保存）
     */
    DroneDTO generateAttributes(String name, String description);
}
