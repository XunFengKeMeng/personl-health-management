package com.example.health.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/4/1 23:11
 * @description: 表单提交数据视图对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionVO {
    /**
     * 表单提交ID
     */
    private Integer submissionId;

    /**
     * 表单模板ID
     */
    private Integer templateId;

    /**
     * 表单模板名
     */
    private String templateName;

    /**
     * 提交用户名
     */
    private String userName;

    /**
     * 审核状态
     */
    private Integer status;

    /**
     * 提交时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submitTime;

    /**
     * 表单项数据
     */
    private List<FormDataVO> formDataList;
}
