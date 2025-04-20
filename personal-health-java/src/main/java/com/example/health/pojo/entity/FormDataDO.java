package com.example.health.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author huanghaiming
 * @date 2025/4/1 23:04
 * @description: 表单数据实体对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormDataDO implements Serializable {
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
     * 用户输入值
     */
    private String value;
}
