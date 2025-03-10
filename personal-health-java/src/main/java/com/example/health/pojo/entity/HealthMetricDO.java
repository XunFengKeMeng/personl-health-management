package com.example.health.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huanghaiming
 * @date 2025/3/5 00:34
 * @description: 健康指标实体类，对应数据库表health_metric
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HealthMetricDO {
    /**
     * 健康指标编号
     */
    private Integer HealthMetricId;

    /**
     * 健康指标名
     */
    private String HealthMetricName;

    /**
     * 健康指标描述
     */
    private String HealthMetricDescription;

    /**
     * 健康指标图标URL
     */
    private String HealthMetricCover;

    /**
     * 健康指标单位
     */
    private String HealthMetricUnit;

    /**
     * 健康指标正常值范围
     */
    private String HealthMetricRange;

}
