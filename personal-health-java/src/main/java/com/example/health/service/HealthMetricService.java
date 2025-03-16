package com.example.health.service;

import com.example.health.api.ApiResponse;
import com.example.health.pojo.dto.HealthMetricDTO;
import com.example.health.pojo.vo.HealthMetricVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/3/16 15:39
 * @description: 健康指标服务
 */
@Service
public interface HealthMetricService {
    /**
     * 新增健康指标
     *
     * @param healthMetricDTO 新增的健康指标信息
     * @return 新增操作响应结果
     */
    ApiResponse<String> save(HealthMetricDTO healthMetricDTO);

    /**
     * 批量删除健康指标
     *
     * @param ids 待删除的健康指标ID集合
     * @return 删除操作响应结果
     */
    ApiResponse<String> deleteMetrics(List<Integer> ids);

    /**
     * 更新健康指标信息
     *
     * @param healthMetricDTO 修改后的健康指标信息
     * @return 更新操作响应结果
     */
    ApiResponse<String> update(HealthMetricDTO healthMetricDTO);

    /**
     * 根据指标名，分页+模糊查询健康指标
     * @param healthMetricDTO 查询参数（部分指标名）
     * @return 查询结果 + 查询响应结果
     */
    ApiResponse<List<HealthMetricVO>> queryMetricsByName(HealthMetricDTO healthMetricDTO);
}
