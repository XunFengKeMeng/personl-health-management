package com.example.health.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author huanghaiming
 * @date 2025/4/4 13:29
 * @description: 提交记录审核状态枚举类
 */
@Getter
@AllArgsConstructor
public enum SubmissionStatusEnum {
    /**
     * 待审核（默认状态）
     */
    PENDING(0, "待审核"),

    /**
     * 审核通过
     */
    APPROVED(1, "审核通过"),

    /**
     * 审核驳回
     */
    REJECTED(2, "审核驳回");

    private final Integer code;
    private final String description;
}
