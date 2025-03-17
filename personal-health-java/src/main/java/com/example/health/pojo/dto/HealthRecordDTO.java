package com.example.health.pojo.dto;

import com.example.health.pojo.dto.query.base.QueryDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

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
    private Integer healthMetricId;

    /**
     * 用户的指标输入值
     */
    private String healthMetricValue;

    /**
     * 健康记录生成时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recordCreateTime;
}
