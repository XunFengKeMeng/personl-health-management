package com.example.health.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author huanghaiming
 * @date 2025/4/1 22:25
 * @description: 表达项数据实体对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormItemDO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 表达项ID
     */
    private Integer itemId;

    /**
     * 表单项名
     */
    private String itemName;

    /**
     * 关联的健康指标ID
     */
    private Integer healthMetricId;

    /**
     * 输入框提示文本
     */
    private String placeholder;

    /**
     * 是否必填
     */
    private Boolean required;

    /**
     * 表单项样式（0文本框1文件上传）
     */
    private Integer style;
}
