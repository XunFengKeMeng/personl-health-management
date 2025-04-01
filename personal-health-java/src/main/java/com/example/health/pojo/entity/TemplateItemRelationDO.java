package com.example.health.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huanghaiming
 * @date 2025/4/1 22:53
 * @description: 表单模板与表单项关联数据的实体对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TemplateItemRelationDO {
    /**
     * 关联ID
     */
    private Integer relationId;

    /**
     * 表单模板ID
     */
    private Integer templateId;

    /**
     * 表单项ID
     */
    private Integer itemId;

    /**
     * 表单项排序号
     */
    private Integer sortOrder;
}
