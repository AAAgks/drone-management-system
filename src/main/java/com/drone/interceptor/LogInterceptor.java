package com.drone.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 请求日志拦截器。
 * <p>
 * 在每个 HTTP 请求前后打印请求信息和耗时，用于开发调试。
 * 使用 {@link ThreadLocal} 存储请求开始时间，保证线程安全。
 */
public class LogInterceptor implements HandlerInterceptor {

    /** 线程局部变量：存储每个请求的开始时间戳 */
    private static final ThreadLocal<Long> START_TIME = new ThreadLocal<>();

    /**
     * 前置处理：记录请求开始时间，打印请求信息。
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        START_TIME.set(System.currentTimeMillis());
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
        System.out.println("==================== 请求开始 ====================");
        System.out.println("时间:    " + timestamp);
        System.out.println("URL:     " + request.getRequestURL());
        System.out.println("方法:    " + request.getMethod());
        System.out.println("IP:      " + getClientIp(request));
        System.out.println("参数:    " + request.getQueryString());
        System.out.println("==================================================");
        return true;
    }

    /**
     * 后置处理：计算耗时，打印请求结果。
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) {
        long duration = System.currentTimeMillis() - START_TIME.get();
        System.out.println("==================== 请求结束 ====================");
        System.out.println("URL:     " + request.getRequestURL());
        System.out.println("耗时:    " + duration + "ms");
        System.out.println("状态码:   " + response.getStatus());
        System.out.println("==================================================");
    }

    /**
     * 完成处理：清除 ThreadLocal，记录异常。
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        START_TIME.remove();
        if (ex != null) {
            System.err.println("请求异常: " + request.getRequestURL() + " - " + ex.getMessage());
        }
    }

    /**
     * 获取客户端真实 IP（支持反向代理场景）。
     * <p>
     * 依次尝试从 X-Forwarded-For、X-Real-IP 头获取，
     * 若都为空则使用直接连接的 remoteAddr。
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
