package com.example.health.pojo.vo;

import com.example.health.pojo.entity.TemplateItemRelationDO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author huanghaiming
 * @date 2025/4/1 22:49
 * @description: 表单模板数据的视图对象（不包含关联项）
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormTemplateVO implements Serializable {
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
     * 创建者名
     */
    private String creatorName;

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
