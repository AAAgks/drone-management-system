package com.drone;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 应用入口。
 * <p>
 * {@code @SpringBootApplication} 启用自动配置和组件扫描（默认扫描本包及子包）。
 * {@code @MapperScan} 扫描 MyBatis Mapper 接口并注册为 Spring Bean。
 */
@SpringBootApplication
@MapperScan("com.drone.mapper") // 扫描 com.drone.mapper 包下的所有 Mapper 接口
public class DroneApplication {

    /**
     * main 方法：启动内嵌 Tomcat 并加载 Spring 容器。
     */
    public static void main(String[] args) {
        SpringApplication.run(DroneApplication.class, args);
    }
}
