package com.example.health.pojo.dto;

import com.example.health.pojo.dto.query.base.QueryDTO;
import lombok.*;

/**
 * @author huanghaiming
 * @date 2025/3/16 14:41
 * @description: 健康指标数据传输对象（与DO一致）
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HealthMetricDTO extends QueryDTO {
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
