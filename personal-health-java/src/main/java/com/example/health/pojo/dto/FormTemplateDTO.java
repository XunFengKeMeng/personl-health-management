package com.example.health.pojo.dto;

import com.example.health.pojo.dto.query.base.QueryDTO;
import lombok.*;

import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/4/1 22:28
 * @description: 表单模板数据传输对象
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormTemplateDTO extends QueryDTO {
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
     * 表单模板包含的表单项列表
     */
    private List<TemplateItemRelationDTO> itemList;
}
