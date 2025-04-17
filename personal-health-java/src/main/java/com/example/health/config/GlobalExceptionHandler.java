package com.example.health.config;

import org.apache.shiro.authz.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author huanghaiming
 * @date 2025/4/16 21:57
 * @description: 全局异常处理器
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 捕获Shiro权限未通过的异常
     *
     * @param e 异常
     * @return 403响应结果
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception e) {
        // 只处理AuthorizationException
        if (e instanceof AuthorizationException) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("status", HttpStatus.FORBIDDEN.value());
            body.put("error", "Forbidden");
            body.put("message", "权限不足，无法访问该资源");
            body.put("timestamp", Instant.now().toString());
            return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
        }
        // 其他异常不处理，允许继续传播
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    }