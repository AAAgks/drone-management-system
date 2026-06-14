package com.drone.config;

import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Apache Shiro 安全框架配置。
 * <p>
 * 使用 {@link IniRealm} 从 classpath 下的 {@code shiro-users.ini} 加载用户信息。
 * 过滤器链全部设为 {@code anon}（匿名访问），认证逻辑由 {@code AuthInterceptor} 拦截器接管，
 * 这样未登录的 API 请求会返回 JSON 格式的 401 错误，而不是跳转到登录页面。
 */
@Configuration
public class ShiroConfig {

    /**
     * 配置基于 INI 文件的 Realm。
     * <p>
     * {@code shiro-users.ini} 中定义用户名、密码和角色，格式为：
     * <pre>
     * [users]
     * username = password, role
     * </pre>
     */
    @Bean
    public IniRealm iniRealm() {
        IniRealm realm = new IniRealm("classpath:shiro-users.ini");
        realm.setCachingEnabled(false); // 关闭缓存，修改 ini 文件后无需重启
        return realm;
    }

    /** 安全管理器，绑定 Realm */
    @Bean
    public DefaultSecurityManager securityManager(IniRealm realm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(realm);
        return manager;
    }

    /**
     * Shiro 过滤器工厂。
     * <p>
     * 当前所有路径设为 {@code anon}，实际认证拦截由 Spring 的
     * {@link com.drone.interceptor.AuthInterceptor} 完成。
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(DefaultSecurityManager securityManager) {
        ShiroFilterFactoryBean filter = new ShiroFilterFactoryBean();
        filter.setSecurityManager(securityManager);

        Map<String, String> chain = new LinkedHashMap<>();
        chain.put("/**", "anon"); // 全部放行，由 AuthInterceptor 做认证

        filter.setFilterChainDefinitionMap(chain);
        return filter;
    }
}
