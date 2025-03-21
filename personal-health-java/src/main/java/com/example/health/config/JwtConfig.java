package com.example.health.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author huanghaiming
 * @date 2025/3/21 12:41
 * @description: 读取application.yml中jwt相关的配置项的配置类
 */
@Component
@ConfigurationProperties(prefix = "jwt")
@Setter
@Getter
public class JwtConfig {
    private String secretKey;
    private long expirationTime;
}
