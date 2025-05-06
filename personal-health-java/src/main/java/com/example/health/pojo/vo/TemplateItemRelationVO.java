package com.example.health.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author huanghaiming
 * @date 2025/4/2 15:13
 * @description: 表单模板下表单项信息的视图对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TemplateItemRelationVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 表单项ID
     */
    private Integer itemId;

    /**
     * 表单项名
     */
    private String itemName;

    /**
     * 输入框提示文本
     */
    private String placeholder;

    /**
     * 是否必填
     */
    private Boolean required;

    /**
     * 表单项排序号
     */
    private Integer sortOrder;

    /**
     * 表单项样式
     */
    private Integer style;
}
