package com.drone.domain.query;

import lombok.Data;

/**
 * 无人机查询条件封装。
 * <p>
 * 作为 Controller 接收前端搜索参数的对象，Spring MVC 会自动绑定同名查询参数。
 */
@Data
public class DroneQuery {

    /** 按名称模糊匹配 */
    private String name;
    /** 按型号模糊匹配 */
    private String model;
    /** 按状态精确匹配（可选值："1"/"0" 或空字符串表示全部） */
    private String status;
    /** 当前页码，默认 1 */
    private int pageNum = 1;
    /** 每页记录数，默认 10 */
    private int pageSize = 10;
}
