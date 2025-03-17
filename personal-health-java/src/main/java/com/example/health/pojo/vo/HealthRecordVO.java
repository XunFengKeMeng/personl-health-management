package com.example.health.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author huanghaiming
 * @date 2025/3/17 09:44
 * @description: 用户健康记录视图对象（于DO相比，多出用户名、指标名字段）
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HealthRecordVO {
    /**
     * 用户健康记录编号
     */
    private Integer recordId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 健康指标名
     */
    private String healthMetricName;

    /**
     * 指标值
     */
    private String healthMetricValue;

    /**
     * 健康记录生成时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recordCreateTime;
}
