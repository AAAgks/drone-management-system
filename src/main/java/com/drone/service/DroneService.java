package com.drone.service;

import com.drone.common.PageResult;
import com.drone.domain.dto.DroneCreateRequest;
import com.drone.domain.dto.DroneDTO;
import com.drone.domain.dto.DroneUpdateRequest;
import com.drone.domain.query.DroneQuery;

/**
 * 无人机业务服务接口。
 * <p>
 * 定义无人机的核心 CRUD 操作契约，由 {@link com.drone.service.impl.DroneServiceImpl} 实现。
 */
public interface DroneService {

    /** 条件分页查询无人机列表 */
    PageResult<DroneDTO> listDrones(DroneQuery query);

    /** 根据 ID 查询单条无人机 */
    DroneDTO getDroneById(Long id);

    /** 新增无人机 */
    void createDrone(DroneCreateRequest request);

    /** 部分更新无人机（只覆盖非 null 字段） */
    void updateDrone(Long id, DroneUpdateRequest request);

    /** 逻辑删除无人机 */
    void deleteDrone(Long id);
}
