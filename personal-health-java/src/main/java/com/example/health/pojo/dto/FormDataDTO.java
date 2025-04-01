package com.example.health.pojo.dto;

import lombok.*;

/**
 * @author huanghaiming
 * @date 2025/4/1 22:55
 * @description: 表单数据传输对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormDataDTO {
    /**
     * 表单数据ID
     */
    private Integer formDataId;

    /**
     * 表单项ID
     */
    private Integer itemId;

    /**
     * 用户输入值
     */
    private String value;
}
