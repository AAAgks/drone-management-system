package com.drone.interceptor;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 认证拦截器。
 * <p>
 * 对 {@code /api/**} 路径进行身份检查（排除登录、登出、当前用户接口）。
 * 未登录的请求返回 JSON 格式的 401 错误，而不是跳转到登录页。
 * <p>
 * 与 Shiro 的 session 机制配合：登录成功后 Shiro 会通过 Cookie 维护会话，
 * 后续请求自动携带 session ID，拦截器通过 {@code Subject.isAuthenticated()} 判断。
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    /** 无需认证即可访问的 API 路径 */
    private static final String[] PUBLIC_PATHS = {"/api/login", "/api/logout", "/api/current-user"};

    /**
     * 前置拦截：检查用户是否已认证。
     *
     * @return true 放行；false 拦截并返回 401 JSON
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // OPTIONS 预检请求直接放行（CORS 需要）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;

        // 公开路径放行
        String path = request.getRequestURI();
        for (String pp : PUBLIC_PATHS) {
            if (path.equals(pp)) return true;
        }

        // 检查 Shiro 会话是否已认证
        Subject subject = SecurityUtils.getSubject();
        if (subject != null && subject.isAuthenticated()) return true;

        // 未登录：返回 JSON 格式的 401 错误
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":401,\"msg\":\"请先登录\"}");
        return false;
    }
}
