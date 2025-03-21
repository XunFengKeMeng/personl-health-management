package com.example.health.controller;

import com.example.health.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author huanghaiming
 * @date 2025/3/21 12:58
 * @description: 测试JWT使用
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    /**
     * 注入JWT
     */
    @Resource
    private JwtUtil jwtUtil;

    /**
     * 生成token
     *
     * @param id 用户ID
     * @param role 用户角色
     * @return 生成的token
     */
    @PostMapping("/toToken")
    @ResponseBody
    public String toToken(@RequestParam Integer id, @RequestParam Integer role) {
        return jwtUtil.toToken(id, role);
    }

    /**
     * 解析token
     * @param token 待解析的token
     * @return token解析结果
     */
    @GetMapping("/fromToken")
    @ResponseBody
    public String fromToken(@RequestParam String token) {
        Claims claims = jwtUtil.fromToken(token);
        if (claims != null) {
            return "Token 有效，用户ID: " + claims.get("id") + "，角色: " + claims.get("role");
        } else {
            return "Token 无效";
        }
    }
}
