package com.example.health.pojo;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author huanghaiming
 * @date 2025/4/11 18:37
 * @description: 实现Shiro的AuthenticationToken接口，目的是将JWT令牌集成到Shiro的认证系统中
 */
public class JwtToken implements AuthenticationToken {
    /**
     * 存储原始的JWT字符串
     * 在本系统中，既作为身份标识，也作为凭证
     */
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    /**
     * 返回身份标识
     *
     * @return token
     */
    @Override
    public Object getPrincipal() {
        return token;
    }

    /**
     * 返回凭证
     *
     * @return token
     */
    @Override
    public Object getCredentials() {
        return token;
    }
}
