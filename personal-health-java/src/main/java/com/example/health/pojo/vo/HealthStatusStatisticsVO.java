package com.example.health.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author huanghaiming
 * @date 2025/5/5 14:02
 * @description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HealthStatusStatisticsVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 健康状态码
     */
    private Integer healthStatus;

    /**
     * 处于某个健康状态下的用户人数
     */
    private Integer count;
}
