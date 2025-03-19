package com.example.health.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author huanghaiming
 * @date 2025/3/19 15:10
 * @description: 跨域配置，为所有接口启用跨域支持
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * 所有地址都可以访问，也可以配置具体地址（生产环境慎用，可能导致CSRF攻击或数据泄露）
     * 允许请求携带任意头部（"GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS"（生产环境慎用，过度开放，可能暴露不必要的危险方法（如DELETE））
     * 跨域允许时间（s）
     * 注意：生产环境避免使用*；若项目集成Spring Security，需额外启用CORS。
     * @param registry 用于添加全局的 CORS 配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("*")
                .maxAge(3600);
    }
}
