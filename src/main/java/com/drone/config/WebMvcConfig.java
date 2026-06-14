package com.drone.config;

import com.drone.interceptor.AuthInterceptor;
import com.drone.interceptor.LogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * Spring MVC 配置。
 * <p>
 * 注册：
 * <ul>
 *   <li>{@link LogInterceptor} — 打印请求日志（所有路径）</li>
 *   <li>{@link AuthInterceptor} — 认证拦截（/api/** 路径，排除登录相关接口）</li>
 * </ul>
 * 同时配置 CORS 跨域，允许前端开发服务器（3000 端口）访问后端 API。
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private AuthInterceptor authInterceptor;

    /**
     * 注册拦截器。
     * <p>
     * 先执行 LogInterceptor（日志），再执行 AuthInterceptor（认证）。
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
     * CORS 跨域配置。
     * <p>
     * 允许任意来源通过任意 HTTP 方法访问，同时允许携带 Cookie（{@code allowCredentials}），
     * 这是 Shiro session 机制正常工作的前提。
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600); // 预检请求缓存 1 小时
    }
}
