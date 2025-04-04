package com.example.health.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author huanghaiming
 * @date 2025/4/1 23:05
 * @description: 表单提交数据实体对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionDO {
    /**
     * 表单提交ID
     */
    private Integer submissionId;

    /**
     * 表单模板ID
     */
    private Integer templateId;

    /**
     * 提交用户ID
     */
    private Integer userId;

    /**
     * 审核状态
     */
    private Integer status;

    /**
     * 提交时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submitTime;
}
