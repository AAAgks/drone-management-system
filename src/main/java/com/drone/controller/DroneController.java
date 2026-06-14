package com.drone.controller;

import com.drone.common.PageResult;
import com.drone.common.R;
import com.drone.domain.dto.DroneCreateRequest;
import com.drone.domain.dto.DroneDTO;
import com.drone.domain.dto.DroneUpdateRequest;
import com.drone.domain.query.DroneQuery;
import com.drone.service.AiAttributeService;
import com.drone.service.DroneService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;

/**
 * 无人机管理 REST 控制器。
 * <p>
 * 所有接口路径以 {@code /api/drones} 为前缀，提供无人机信息的 CRUD 操作
 * 以及 AI 智能属性生成功能。
 */
@RestController
@RequestMapping("/api/drones")
public class DroneController {

    /** 无人机业务服务 */
    @Resource
    private DroneService droneService;

    /** AI 属性生成服务 */
    @Resource
    private AiAttributeService aiAttributeService;

    /**
     * 条件分页查询无人机列表。
     * <p>
     * 支持按名称、型号、状态筛选，参数通过 URL Query String 传入，
     * Spring MVC 自动绑定到 {@link DroneQuery} 对象。
     *
     * @param query 查询条件（含分页参数）
     * @return 分页结果
     */
    @GetMapping
    public R<PageResult<DroneDTO>> list(DroneQuery query) {
        PageResult<DroneDTO> page = droneService.listDrones(query);
        return R.ok(page);
    }

    /**
     * 根据主键 ID 查询单条无人机。
     *
     * @param id 无人机 ID（路径变量）
     * @return 无人机详情 DTO
     */
    @GetMapping("/{id}")
    public R<DroneDTO> getById(@PathVariable Long id) {
        DroneDTO dto = droneService.getDroneById(id);
        return R.ok(dto);
    }

    /**
     * 新增无人机。
     *
     * @param request 新增请求体（含校验注解）
     * @return 成功提示
     */
    @PostMapping
    public R<String> create(@Valid @RequestBody DroneCreateRequest request) {
        droneService.createDrone(request);
        return R.ok("新增成功");
    }

    /**
     * 修改无人机。
     * <p>
     * 采用部分更新策略：只覆盖请求中非 null 的字段。
     *
     * @param id      无人机 ID
     * @param request 修改请求体
     * @return 成功提示
     */
    @PutMapping("/{id}")
    public R<String> update(@PathVariable Long id, @Valid @RequestBody DroneUpdateRequest request) {
        droneService.updateDrone(id, request);
        return R.ok("修改成功");
    }

    /**
     * 逻辑删除无人机（将 {@code deleted} 字段设为 1）。
     *
     * @param id 无人机 ID
     * @return 成功提示
     */
    @DeleteMapping("/{id}")
    public R<String> delete(@PathVariable Long id) {
        droneService.deleteDrone(id);
        return R.ok("删除成功");
    }

    /**
     * AI 智能生成无人机属性。
     * <p>
     * 根据无人机名称和描述，自动推断等级（消费级/工业级/军用级），
     * 在合理范围内随机生成各项参数。
     *
     * @param body 包含 "name" 和 "description" 的 JSON 对象
     * @return 填充了属性的 DTO（id 为 null，需用户确认后保存）
     */
    @PostMapping("/ai-generate")
    public R<DroneDTO> aiGenerate(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String description = body.get("description");
        DroneDTO generated = aiAttributeService.generateAttributes(name, description);
        return R.ok(generated);
    }
}
