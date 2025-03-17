package com.example.health.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huanghaiming
 * @date 2025/3/16 14:44
 * @description: 健康指标视图对象（与DO一致）
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HealthMetricVO {
    /**
     * 健康指标编号
     */
    private Integer healthMetricId;

    /**
     * 健康指标名
     */
    private String healthMetricName;

    /**
     * 健康指标描述
     */
    private String healthMetricDescription;

    /**
     * 健康指标图标URL
     */
    private String healthMetricCover;

    /**
     * 健康指标单位
     */
    private String healthMetricUnit;

    /**
     * 健康指标正常值范围
     */
    private String healthMetricRange;
}
