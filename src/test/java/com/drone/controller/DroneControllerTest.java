package com.drone.controller;

import com.drone.common.PageResult;
import com.drone.domain.dto.DroneCreateRequest;
import com.drone.domain.dto.DroneDTO;
import com.drone.interceptor.AuthInterceptor;
import com.drone.service.AiAttributeService;
import com.drone.service.DroneService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * DroneController 集成测试。
 * 使用 H2 内存数据库 + 全量 Spring 上下文，
 * {@code @MockBean AuthInterceptor} 始终放行，绕过 Shiro 认证。
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DroneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DroneService droneService;

    @MockBean
    private AiAttributeService aiAttributeService;

    /** 核心：Mock 掉 AuthInterceptor，让所有请求通过 */
    @MockBean
    private AuthInterceptor authInterceptor;

    @BeforeEach
    void setupMockAuth() throws Exception {
        when(authInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    // ====== GET /api/drones ======

    @Test
    void should_return200WithPage_when_listCalled() throws Exception {
        PageResult<DroneDTO> page = PageResult.of(
                new com.github.pagehelper.PageInfo<>(Collections.emptyList()));
        when(droneService.listDrones(any())).thenReturn(page);

        mockMvc.perform(get("/api/drones?pageNum=1&pageSize=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists());
    }

    // ====== GET /api/drones/{id} ======

    @Test
    void should_return200_when_droneExists() throws Exception {
        when(droneService.getDroneById(1L)).thenReturn(buildDto(1L));

        mockMvc.perform(get("/api/drones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.name").value("大疆 Air 3"));
    }

    // ====== POST /api/drones ======

    @Test
    void should_return200_when_createValid() throws Exception {
        DroneCreateRequest req = new DroneCreateRequest();
        req.setName("新无人机");
        req.setModel("X-100");
        req.setStatus("正常");
        doNothing().when(droneService).createDrone(any());

        mockMvc.perform(post("/api/drones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    // ====== DELETE /api/drones/{id} ======

    @Test
    void should_return200_when_deleteSuccess() throws Exception {
        doNothing().when(droneService).deleteDrone(1L);

        mockMvc.perform(delete("/api/drones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    // ====== POST /api/drones/ai-generate ======

    @Test
    void should_returnGeneratedDto_when_aiGenerate() throws Exception {
        DroneDTO dto = buildDto(null);
        dto.setModel("军用级");
        when(aiAttributeService.generateAttributes(any(), any())).thenReturn(dto);

        mockMvc.perform(post("/api/drones/ai-generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"侦察无人机\",\"description\":\"军用\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.model").value("军用级"));
    }

    // ====== POST /api/login ======

    @Test
    void should_return401_when_badCredentials() throws Exception {
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"wrong\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(401));
    }

    private DroneDTO buildDto(Long id) {
        return DroneDTO.builder()
                .id(id).name("大疆 Air 3").model("Air 3").status(1)
                .statusText("正常").aiGenerated(0)
                .build();
    }
}
