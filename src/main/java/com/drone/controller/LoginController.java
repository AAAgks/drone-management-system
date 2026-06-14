package com.drone.controller;

import com.drone.common.R;
import com.drone.domain.dto.LoginRequest;
import com.drone.service.LoginService;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 登录认证控制器。
 * <p>
 * 职责：接收 HTTP 请求 → 参数校验 → 调 Service → 返回 JSON。
 * 认证逻辑全部在 {@link LoginService} 中。
 */
@RestController
@RequestMapping("/api")
public class LoginController {

    @Resource
    private LoginService loginService;

    /**
     * 用户登录。
     *
     * @param request 包含 username 和 password（@Valid 自动校验必填）
     * @return 成功或失败
     */
    @PostMapping("/login")
    public R<String> login(@Valid @RequestBody LoginRequest request) {
        try {
            loginService.login(request);
            return R.ok("登录成功");
        } catch (AuthenticationException e) {
            return R.fail(401, "用户名或密码错误");
        }
    }

    /** 用户登出 */
    @PostMapping("/logout")
    public R<String> logout() {
        loginService.logout();
        return R.ok("已退出");
    }

    /**
     * 获取当前登录用户（页面刷新时恢复会话）。
     *
     * @return 已登录→用户名，未登录→401
     */
    @GetMapping("/current-user")
    public R<String> currentUser() {
        String username = loginService.getCurrentUser();
        if (username != null) {
            return R.ok(username);
        }
        return R.fail(401, "未登录");
    }
}
