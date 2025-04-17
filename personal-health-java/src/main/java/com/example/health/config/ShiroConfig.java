package com.example.health.config;

import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author huanghaiming
 * @date 2025/4/11 20:09
 * @description: Shiro配置类
 */
@Configuration
public class ShiroConfig {
    /**
     * 配置Shiro的核心过滤器工厂（用于创建Shiro过滤器）
     * 配置访问资源需要的权限
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 将安全管理器注册到过滤器中
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 添加自定义filter
        Map<String, Filter> filters = new HashMap<>();
        filters.put("jwt", new JwtFilter());
        shiroFilterFactoryBean.setFilters(filters);

        // 过滤链的定义与存储
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        // 开放接口：登录、注册、令牌验证
        filterChainDefinitionMap.put("/api/health-management/user/login", "anon");
        filterChainDefinitionMap.put("/api/health-management/user/register", "anon");
        filterChainDefinitionMap.put("/api/health-management/user/auth", "anon");

        // 发现问题：以下代码本应根据最后的roles要求去拦截，但实际情况中却没有。似乎加入和不加入结果一样。
//        filterChainDefinitionMap.put("/api/health-management/user/**", "jwt, roles[admin]");

        // 过滤链设置
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 配置ModularRealmAuthenticator
     */
    @Bean
    public ModularRealmAuthenticator authenticator() {
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        // 可配置认证策略，默认AtLeastOneSuccessfulStrategy
        AuthenticationStrategy strategy = new FirstSuccessfulStrategy();
        authenticator.setAuthenticationStrategy(strategy);
        return authenticator;
    }

    /**
     * 坑。不加这个启动会报错
     *
     * @return
     */
    @Bean
    public Authorizer authorizer(){
        return new ModularRealmAuthorizer();
    }

    /**
     * 配置安全管理器，并设置使用的Realm
     */
    @Bean
    public DefaultWebSecurityManager securityManager(JwtRealm jwtRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 注意，设置realm要在设置authenticator之后，否则会报错：Configuration error: No realms have been configured
        securityManager.setAuthenticator(authenticator());
        securityManager.setRealm(jwtRealm);
        return securityManager;
    }

    @Bean
    public JwtRealm jwtRealm() {
        return new JwtRealm();
    }

    /**
     * Shiro生命周期处理器
     * 确保Shiro组件能被正确初始化和摧毁
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 为Spring Boot开启对Shiro注解的支持
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }
}
