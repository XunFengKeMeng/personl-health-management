package com.example.health.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author huanghaiming
 * @date 2025/4/1 22:26
 * @description: 表单项数据视图对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormItemVO implements Serializable {
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
     * 关联的健康指标名
     */
    private String healthMetricName;

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
