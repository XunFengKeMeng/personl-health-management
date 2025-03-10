package com.example.health.pojo.dto.query.extend;

import com.example.health.pojo.dto.query.base.QueryDTO;
import lombok.*;

/**
 * @author huanghaiming
 * @date 2025/3/5 14:30
 * @description: 健康指标查询DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HealthMetricQueryDTO extends QueryDTO {
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
     * 健康指标单位
     */
    private String HealthMetricUnit;

}
