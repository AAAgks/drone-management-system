package com.drone.mapper;

import com.drone.domain.entity.Drone;
import com.drone.domain.query.DroneQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 无人机数据访问接口（MyBatis Mapper）。
 * <p>
 * MyBatis 会自动将接口方法与 {@code DroneMapper.xml} 中的 SQL 绑定。
 * 支持 MySQL 和 SQLite 双数据库，具体 SQL 由 {@code databaseId} 自动选择。
 */
public interface DroneMapper {

    /**
     * 按条件查询无人机列表（支持模糊匹配）。
     * <p>
     * 参数来自 {@link DroneQuery}，由 PageHelper 自动追加分页。
     */
    List<Drone> selectByCondition(DroneQuery query);

    /**
     * 按主键查询单条记录（排除已逻辑删除的数据）。
     */
    Drone selectById(@Param("id") Long id);

    /** 新增无人机，主键回填到 {@code drone.id} */
    int insert(Drone drone);

    /** 按主键更新 */
    int update(Drone drone);

    /**
     * 逻辑删除：将 {@code deleted} 字段设为 1，而非物理删除。
     */
    int deleteById(@Param("id") Long id);
}
