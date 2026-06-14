package com.drone.service.impl;

import com.drone.common.Constants;
import com.drone.common.PageResult;
import com.drone.domain.dto.DroneCreateRequest;
import com.drone.domain.dto.DroneDTO;
import com.drone.domain.dto.DroneUpdateRequest;
import com.drone.domain.entity.Drone;
import com.drone.domain.query.DroneQuery;
import com.drone.exception.BusinessException;
import com.drone.mapper.DroneMapper;
import com.drone.service.DroneService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 无人机信息管理业务服务实现。
 * <p>
 * 核心业务流程：
 * <ol>
 *   <li>调用 PageHelper 实现物理分页</li>
 *   <li>实体与 DTO 之间的转换</li>
 *   <li>通过 {@code @Transactional} 保证写操作的事务一致性</li>
 * </ol>
 */
@Service
public class DroneServiceImpl implements DroneService {

    private static final Logger log = LoggerFactory.getLogger(DroneServiceImpl.class);

    @Resource
    private DroneMapper droneMapper;

    /**
     * 条件分页查询。
     * <p>
     * 使用 PageHelper 的 {@code startPage} 方法启动分页拦截，
     * 紧跟着的第一次 MyBatis 查询会被自动加入 LIMIT 子句。
     */
    @Override
    public PageResult<DroneDTO> listDrones(DroneQuery query) {
        // 启动分页拦截
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        // 执行查询（PageHelper 自动包装 SQL 加上 LIMIT）
        List<Drone> list = droneMapper.selectByCondition(query);
        PageInfo<Drone> pageInfo = new PageInfo<>(list);
        // 实体列表 → DTO 列表
        List<DroneDTO> dtoList = list.stream()
                .map(DroneDTO::fromEntity)
                .collect(Collectors.toList());
        // 构建分页结果
        PageInfo<DroneDTO> dtoPageInfo = new PageInfo<>(dtoList);
        dtoPageInfo.setTotal(pageInfo.getTotal());
        dtoPageInfo.setPages(pageInfo.getPages());
        return PageResult.of(dtoPageInfo);
    }

    /**
     * 根据 ID 查询。
     *
     * @throws BusinessException 如果记录不存在
     */
    @Override
    public DroneDTO getDroneById(Long id) {
        Drone drone = droneMapper.selectById(id);
        if (drone == null) {
            throw new BusinessException(404, "无人机不存在，id=" + id);
        }
        return DroneDTO.fromEntity(drone);
    }

    /**
     * 新增无人机。
     * <p>
     * 将请求 DTO 转为数据库实体，设置默认值（deleted=0、aiGenerated=0），
     * 将中文状态文本转为数字。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDrone(DroneCreateRequest request) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime purchase = null;
        if (request.getPurchaseDate() != null && !request.getPurchaseDate().isEmpty()) {
            purchase = LocalDate.parse(request.getPurchaseDate(), DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
        }
        Drone drone = Drone.builder()
                .name(request.getName())
                .model(request.getModel())
                .serialNumber(request.getSerialNumber())
                .manufacturer(request.getManufacturer())
                .type(request.getType())
                .weight(request.getWeight())
                .maxSpeed(request.getMaxSpeed())
                .maxAltitude(request.getMaxAltitude())
                .endurance(request.getEndurance())
                .rangeDistance(request.getRangeDistance())
                .maxPayload(request.getMaxPayload())
                .cameraType(request.getCameraType())
                .batteryCapacity(request.getBatteryCapacity())
                .gpsAccuracy(request.getGpsAccuracy())
                .operatingTemperature(request.getOperatingTemperature())
                .waterproofLevel(request.getWaterproofLevel())
                .status("正常".equals(request.getStatus()) ? Constants.STATUS_NORMAL : Constants.STATUS_DISABLED)
                .description(request.getDescription())
                .aiGenerated(Constants.AI_MANUAL)
                .deleted(0)
                .createTime(now)
                .updateTime(now)
                .purchaseDate(purchase)
                .build();
        droneMapper.insert(drone);
        log.info("新增无人机成功，id={}, name={}", drone.getId(), drone.getName());
    }

    /**
     * 修改无人机。
     * <p>
     * 先查询现有记录，再逐一检查请求字段是否为 null —— 只覆盖有值的字段。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDrone(Long id, DroneUpdateRequest request) {
        Drone existing = droneMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(404, "无人机不存在，id=" + id);
        }

        existing.setId(id);
        // 部分更新：只覆盖 request 中非 null 的字段
        setIfPresent(existing::setName, request.getName());
        setIfPresent(existing::setModel, request.getModel());
        setIfPresent(existing::setSerialNumber, request.getSerialNumber());
        setIfPresent(existing::setManufacturer, request.getManufacturer());
        setIfPresent(existing::setType, request.getType());
        setIfPresent(existing::setWeight, request.getWeight());
        setIfPresent(existing::setMaxSpeed, request.getMaxSpeed());
        setIfPresent(existing::setMaxAltitude, request.getMaxAltitude());
        setIfPresent(existing::setEndurance, request.getEndurance());
        setIfPresent(existing::setRangeDistance, request.getRangeDistance());
        setIfPresent(existing::setMaxPayload, request.getMaxPayload());
        setIfPresent(existing::setCameraType, request.getCameraType());
        setIfPresent(existing::setBatteryCapacity, request.getBatteryCapacity());
        setIfPresent(existing::setGpsAccuracy, request.getGpsAccuracy());
        setIfPresent(existing::setOperatingTemperature, request.getOperatingTemperature());
        setIfPresent(existing::setWaterproofLevel, request.getWaterproofLevel());
        if (request.getStatus() != null) {
            existing.setStatus("正常".equals(request.getStatus()) ? Constants.STATUS_NORMAL : Constants.STATUS_DISABLED);
        }
        setIfPresent(existing::setDescription, request.getDescription());
        if (request.getPurchaseDate() != null && !request.getPurchaseDate().isEmpty()) {
            existing.setPurchaseDate(LocalDate.parse(request.getPurchaseDate(), DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay());
        }
        existing.setUpdateTime(LocalDateTime.now());

        droneMapper.update(existing);
        log.info("修改无人机成功，id={}", id);
    }

    /**
     * 逻辑删除：将 {@code deleted} 字段设为 1，不真正删除物理记录。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDrone(Long id) {
        Drone existing = droneMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(404, "无人机不存在，id=" + id);
        }
        droneMapper.deleteById(id);
        log.info("逻辑删除无人机成功，id={}", id);
    }

    /** 仅当 value 非 null 时才执行 setter */
    private static <T> void setIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
