package com.example.health.config;

import com.example.health.pojo.JwtToken;
import com.example.health.service.UserService;
import com.example.health.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author huanghaiming
 * @date 2025/4/11 18:30
 * @description: Shiro Realm负责从应用配置的数据源中获取用户的认证信息以及授权信息
 */
@Component
public class JwtRealm extends AuthorizingRealm {
    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 认证
     *
     * @param principals 认证令牌（封装了用户的登录信息）
     * @return 认证信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 提取token
        String token = (String) principals.getPrimaryPrincipal();
        // 解析token
        Claims claims = jwtUtil.fromToken(token);

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 根据角色ID添加角色。
        // 这里的Number.class不能直接用Integer.class代替并把后面的.iniValue()删去。可能是因为claims存储的role值为String类型。
        int roleId = claims.get("role", Number.class).intValue();
        String roleName = roleId == 1 ? "admin" : "user";
        authorizationInfo.addRole(roleName);
        // 管理员同时拥有用户身份
        if ("admin".equals(roleName)) {
            authorizationInfo.addRole("user");
        }
        // 返回认证信息
        return authorizationInfo;
    }

    /**
     * 授权
     *
     * @param authenticationToken 身份标识集合（存储用户身份信息）
     * @return 授权信息
     * @throws AuthenticationException Shiro认证异常基类，有多个子类
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 提取token
        JwtToken jwtToken = (JwtToken) authenticationToken;
        String token = (String) jwtToken.getCredentials();
        // 解析token
        Claims claims = jwtUtil.fromToken(token);
        if (claims == null) {
            throw new AuthenticationException("token无效");
        }
        if (claims.getExpiration().before(new Date())) {
            throw new ExpiredCredentialsException("token已过期");
        }
        // 进行双重验证：数据格式验证、业务逻辑验证（数据库）
        Integer userId = claims.get("id", Integer.class);
        if (userId == null || userService.getByUserId(userId) == null) {
            throw new UnknownAccountException("用户不存在");
        }
        // 构建授权信息
        return new SimpleAuthenticationInfo(token, token, getName());
    }
}
