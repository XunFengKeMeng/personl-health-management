package com.example.health.pojo.dto;

import lombok.*;

/**
 * @author huanghaiming
 * @date 2025/4/1 22:39
 * @description: 表单模板与表单项关联数据的传输对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TemplateItemRelationDTO {
    /**
     * 表单项ID
     */
    private Integer itemId;

    /**
     * 表单项排序号
     */
    private Integer sortOrder;
}
