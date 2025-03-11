package com.example.health.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author huanghaiming
 * @date 2025/3/11 14:58
 * @description: 用户账号状态枚举
 */
@Getter
@AllArgsConstructor
public enum ActiveStatusEnum {
    ACTIVE(true, "账号已激活"),
    INACTIVE(false, "账号未激活");

    /**
     * 用户账号状态编码
     */
    private final Boolean statusCode;
    /**
     * 用户账号状态名
     */
    private final String statusDescription;
}
