package com.example.health.pojo.dto;

import com.example.health.pojo.dto.query.base.QueryDTO;
import lombok.*;

import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/4/1 22:57
 * @description: 表单提交数据传输对象
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionDTO extends QueryDTO {
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
    private Boolean status;

    /**
     * 表单项数据
     */
    private List<FormDataDTO> formDataList;
}
