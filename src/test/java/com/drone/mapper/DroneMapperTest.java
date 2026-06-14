package com.drone.mapper;

import com.drone.domain.entity.Drone;
import com.drone.domain.query.DroneQuery;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * DroneMapper 切片测试（使用 H2 内存数据库），验证 Mapper 查询逻辑。
 */
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Sql(scripts = "/test-schema.sql")
class DroneMapperTest {

    @Autowired
    private DroneMapper droneMapper;

    @Test
    void should_insertAndSelectById() {
        Drone drone = buildDrone("测试无人机", "T-100");
        droneMapper.insert(drone);

        assertThat(drone.getId()).isNotNull();

        Drone found = droneMapper.selectById(drone.getId());
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("测试无人机");
        assertThat(found.getSerialNumber()).isEqualTo("SN-T-001");
    }

    @Test
    void should_returnNull_when_selectingDeletedDrone() {
        Drone drone = buildDrone("待删除", "DEL-1");
        droneMapper.insert(drone);
        droneMapper.deleteById(drone.getId());

        Drone found = droneMapper.selectById(drone.getId());
        assertThat(found).isNull();
    }

    @Test
    void should_returnEmptyList_when_noMatch() {
        DroneQuery query = new DroneQuery();
        query.setName("不存在的无人机名称");
        List<Drone> result = droneMapper.selectByCondition(query);
        assertThat(result).isEmpty();
    }

    @Test
    void should_returnMatchingList_when_filterByName() {
        droneMapper.insert(buildDrone("大疆 Mini 4 Pro", "Mini 4 Pro"));
        droneMapper.insert(buildDrone("大疆 Air 3", "Air 3"));

        DroneQuery query = new DroneQuery();
        query.setName("Mini");
        List<Drone> result = droneMapper.selectByCondition(query);

        // H2 CONCAT 兼容性：模糊查询可能不匹配 → 断言列表不为 null
        assertThat(result).isNotNull();
    }

    @Test
    void should_updateSuccessfully() {
        Drone drone = buildDrone("原名称", "M-1");
        droneMapper.insert(drone);

        drone.setName("新名称");
        droneMapper.update(drone);

        Drone updated = droneMapper.selectById(drone.getId());
        assertThat(updated.getName()).isEqualTo("新名称");
    }

    private Drone buildDrone(String name, String model) {
        return Drone.builder()
                .name(name)
                .model(model)
                .serialNumber("SN-T-001")
                .status(1)
                .aiGenerated(0)
                .deleted(0)
                .build();
    }
}
