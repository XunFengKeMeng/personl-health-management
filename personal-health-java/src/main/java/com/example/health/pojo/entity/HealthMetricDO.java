package com.example.health.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author huanghaiming
 * @date 2025/3/5 00:34
 * @description: 健康指标实体类，对应数据库表health_metric
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HealthMetricDO implements Serializable {
    private static final long serialVersionUID = 1L;
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
