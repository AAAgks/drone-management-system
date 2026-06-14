package com.drone.service.impl;

import com.drone.common.PageResult;
import com.drone.domain.dto.DroneCreateRequest;
import com.drone.domain.dto.DroneDTO;
import com.drone.domain.dto.DroneUpdateRequest;
import com.drone.domain.entity.Drone;
import com.drone.domain.query.DroneQuery;
import com.drone.exception.BusinessException;
import com.drone.mapper.DroneMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * DroneServiceImpl 单元测试（Mockito Mock Mapper）。
 */
@ExtendWith(MockitoExtension.class)
class DroneServiceImplTest {

    @Mock
    private DroneMapper droneMapper;

    @InjectMocks
    private DroneServiceImpl droneService;

    // ====== listDrones ======

    @Test
    void should_returnEmptyPage_when_noData() {
        when(droneMapper.selectByCondition(any(DroneQuery.class)))
                .thenReturn(Collections.emptyList());

        PageResult<DroneDTO> result = droneService.listDrones(new DroneQuery());

        assertThat(result).isNotNull();
        assertThat(result.getRows()).isEmpty();
    }

    @Test
    void should_returnPage_when_dataExists() {
        Drone drone = buildDrone(1L, "大疆 Air 3");
        when(droneMapper.selectByCondition(any()))
                .thenReturn(Collections.singletonList(drone));

        PageResult<DroneDTO> result = droneService.listDrones(new DroneQuery());

        assertThat(result.getRows()).hasSize(1);
        assertThat(result.getRows().get(0).getName()).isEqualTo("大疆 Air 3");
    }

    // ====== getDroneById ======

    @Test
    void should_returnDto_when_droneExists() {
        when(droneMapper.selectById(1L)).thenReturn(buildDrone(1L, "大疆 Mavic 3"));

        DroneDTO dto = droneService.getDroneById(1L);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("大疆 Mavic 3");
        assertThat(dto.getStatusText()).isEqualTo("正常");
    }

    @Test
    void should_throwBusinessException_when_droneNotFound() {
        when(droneMapper.selectById(99L)).thenReturn(null);

        assertThatThrownBy(() -> droneService.getDroneById(99L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("不存在");
    }

    // ====== createDrone ======

    @Test
    void should_insertDrone_when_requestIsValid() {
        DroneCreateRequest req = new DroneCreateRequest();
        req.setName("新无人机");
        req.setModel("X-200");
        req.setStatus("正常");
        when(droneMapper.insert(any(Drone.class))).thenReturn(1);

        droneService.createDrone(req);

        verify(droneMapper, times(1)).insert(any(Drone.class));
    }

    // ====== updateDrone ======

    @Test
    void should_update_when_droneExists() {
        when(droneMapper.selectById(1L)).thenReturn(buildDrone(1L, "旧名称"));
        when(droneMapper.update(any(Drone.class))).thenReturn(1);

        DroneUpdateRequest req = new DroneUpdateRequest();
        req.setName("新名称");
        droneService.updateDrone(1L, req);

        verify(droneMapper, times(1)).update(any(Drone.class));
    }

    @Test
    void should_throwException_when_updateNonExisting() {
        when(droneMapper.selectById(99L)).thenReturn(null);

        DroneUpdateRequest req = new DroneUpdateRequest();
        req.setName("x");

        assertThatThrownBy(() -> droneService.updateDrone(99L, req))
                .isInstanceOf(BusinessException.class);
    }

    // ====== deleteDrone ======

    @Test
    void should_delete_when_droneExists() {
        when(droneMapper.selectById(1L)).thenReturn(buildDrone(1L, "待删除"));
        when(droneMapper.deleteById(1L)).thenReturn(1);

        droneService.deleteDrone(1L);

        verify(droneMapper, times(1)).deleteById(1L);
    }

    @Test
    void should_throwException_when_deleteNonExisting() {
        when(droneMapper.selectById(99L)).thenReturn(null);

        assertThatThrownBy(() -> droneService.deleteDrone(99L))
                .isInstanceOf(BusinessException.class);
    }

    // ====== helper ======

    private Drone buildDrone(Long id, String name) {
        return Drone.builder()
                .id(id).name(name).model("M1")
                .serialNumber("SN-" + id)
                .status(1).aiGenerated(0).deleted(0)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
    }
}
