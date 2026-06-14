package com.drone.service;

import com.drone.domain.dto.LoginRequest;

/**
 * 登录认证服务接口。
 */
public interface LoginService {

    /**
     * 用户登录。
     *
     * @param request 包含用户名和密码
     * @return 登录成功返回用户名
     * @throws org.apache.shiro.authc.AuthenticationException 认证失败
     */
    String login(LoginRequest request);

    /** 登出 */
    void logout();

    /** 获取当前登录用户名（未登录返回 null） */
    String getCurrentUser();
}
