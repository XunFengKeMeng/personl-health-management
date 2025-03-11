package com.example.health.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author huanghaiming
 * @date 2025/3/11 18:49
 * @description: 响应状态码
 */
@Getter
@AllArgsConstructor
public enum HttpStatusEnum {
    OK(200, "OK"),
    BAD_REQUEST(400, "Bad Request");

    /**
     * 响应状态码
     */
    private final Integer httpStatusCode;
    /**
     * 响应状态信息
     */
    private final String httpStatusDescription;
}
