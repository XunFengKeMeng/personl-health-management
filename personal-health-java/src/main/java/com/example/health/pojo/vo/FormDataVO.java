package com.example.health.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author huanghaiming
 * @date 2025/4/3 19:45
 * @description: 提交表单数据项视图对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormDataVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 表单数据ID
     */
    private Integer formDataId;

    /**
     * 提交记录ID
     */
    private Integer submissionId;

    /**
     * 表单项ID
     */
    private Integer itemId;

    /**
     * 表单项名
     */
    private String itemName;

    /**
     * 是否必填
     */
    private Boolean required;

    /**
     * 用户输入值
     */
    private String value;

    /**
     * 表单项排序号
     */
    private Integer sortOrder;
}
