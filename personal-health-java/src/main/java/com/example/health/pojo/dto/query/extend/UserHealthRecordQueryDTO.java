package com.example.health.pojo.dto.query.extend;

import com.example.health.pojo.dto.query.base.QueryDTO;
import lombok.*;

/**
 * @author huanghaiming
 * @date 2025/3/5 14:43
 * @description: 用户健康记录查询DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserHealthRecordQueryDTO extends QueryDTO {
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

}
