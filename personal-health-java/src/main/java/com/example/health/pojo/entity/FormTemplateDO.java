package com.example.health.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author huanghaiming
 * @date 2025/4/1 22:44
 * @description: 表单模板数据实体对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormTemplateDO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 表单模板ID
     */
    private Integer templateId;

    /**
     * 表单模板名
     */
    private String templateName;

    /**
     * 创建者ID
     */
    private Integer creatorId;

    /**
     * 是否发布
     */
    private Boolean active;

    /**
     * 表单模板创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
