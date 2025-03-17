package com.example.health.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author huanghaiming
 * @date 2025/3/17 15:00
 * @description: 通知是否已读
 */
@Getter
@AllArgsConstructor
public enum ReadStatusEnum {
    READ(true, "已读"),
    UNREAD(false, "未读");

    /**
     * 通知是否已读
     */
    private final Boolean readStatus;
    /**
     * 通知是否已读描述信息
     */
    private final String readStatusDescription;
}
