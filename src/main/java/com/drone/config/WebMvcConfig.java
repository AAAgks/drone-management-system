package com.drone.config;

import com.drone.interceptor.AuthInterceptor;
import com.drone.interceptor.LogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * Spring MVC 配置（前后端不分离模式）。
 * <p>
 * 注册：
 * <ul>
 *   <li>{@link LogInterceptor} — 打印请求日志（所有路径）</li>
 *   <li>{@link AuthInterceptor} — 认证拦截（/api/** 路径，排除登录相关接口）</li>
 * </ul>
 * <p>
 * 前后端同源部署，无需 CORS 跨域配置。前端静态资源由 Spring Boot 从
 * {@code classpath:/static/} 目录直接提供服务。
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private AuthInterceptor authInterceptor;

    /**
     * 注册拦截器。
     * <p>
     * 先执行 LogInterceptor（日志），再执行 AuthInterceptor（认证）。
     * 注意：不拦截静态资源路径，只拦截 /api/** 的 API 请求。
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .addPathPatterns("/**");
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/login", "/api/logout", "/api/current-user");
    }

    /**
     * 静态资源处理器。
     * <p>
     * 将 {@code classpath:/static/} 映射到根路径，Spring Boot 默认已包含此行为，
     * 此处显式声明以支持 SPA hash 路由：未匹配到静态文件时回落到 index.html。
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}
