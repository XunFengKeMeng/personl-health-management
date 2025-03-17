package com.example.health.pojo.dto;

import com.example.health.pojo.dto.query.base.QueryDTO;
import lombok.*;

/**
 * @author huanghaiming
 * @date 2025/3/17 09:41
 * @description: 用户健康记录数据传输对象
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HealthRecordDTO extends QueryDTO {
    /**
     * 用户健康记录编号
     */
    private Integer recordId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 健康指标ID
     */
    private Integer HealthMetricId;

    /**
     * 用户的指标输入值
     */
    private String HealthMetricValue;
}
