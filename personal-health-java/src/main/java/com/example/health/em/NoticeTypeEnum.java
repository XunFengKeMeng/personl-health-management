package com.example.health.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author huanghaiming
 * @date 2025/3/17 15:11
 * @description: 通知类型枚举类
 */
@Getter
@AllArgsConstructor
public enum NoticeTypeEnum {
    HEALTH_ALERT(1, "健康指标异常提示"),
    ACTIVITY_NOTICE(2, "活动提示"),
    VERSION_UPDATE(3, "版本更新提示");

    /**
     * 响应状态码
     */
    private final Integer noticeTypeCode;

    /**
     * 响应状态信息
     */
    private final String noticeTypeDescription;
}
