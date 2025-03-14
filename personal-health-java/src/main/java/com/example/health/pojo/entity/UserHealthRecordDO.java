package com.example.health.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author huanghaiming
 * @date 2025/3/5 00:35
 * @description: 用户健康记录实体类，对应数据库表user_health_record
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserHealthRecordDO {
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

    /**
     * 健康记录生成时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recordCreateTime;
}
