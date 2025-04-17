package com.example.health.utils;


import com.example.health.config.JwtConfig;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * @author huanghaiming
 * @date 2025/3/21 12:37
 * @description: 用于生成和解析JWT的工具类
 */
@Component
public class JwtUtil {
    private final JwtConfig jwtConfig;

    /**
     * 注入JwtConfig
     */
    @Autowired
    public JwtUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    /**
     * 生成 token(JWT)
     * 声明令牌类型为JWT，签名算法采用HS256。
     * 设置有效信息（Payload）:用户ID、用户角色、主题、令牌过期时间、使用UUID生成的JWT的唯一标识。
     * 使用HS256算法和密钥对Header和Payload进行签名。
     *
     * @param id   用户ID
     * @param role 用户角色
     * @return String
     */
    public String toToken(Integer id, Integer role) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .claim("id", id)
                .claim("role", role)
                .setSubject("用户认证")
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpirationTime()))
                .setId(UUID.randomUUID().toString())
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecretKey())
                .compact();
    }

    /**
     * 解析JWT,提取有效信息（Payload）
     *
     * @param token 需要解析的JWT
     */
    public Claims fromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtConfig.getSecretKey())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            System.out.println("Token 已过期");
            return null;
        } catch (SignatureException e) {
            System.out.println("Token 签名无效");
            return null;
        } catch (Exception e) {
            System.out.println("Token 解析失败");
            return null;
        }
    }
}
