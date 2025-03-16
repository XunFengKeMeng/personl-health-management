package com.example.health.service.impl;

import com.example.health.api.ApiResponse;
import com.example.health.mapper.HealthMetricMapper;
import com.example.health.pojo.dto.HealthMetricDTO;
import com.example.health.pojo.entity.HealthMetricDO;
import com.example.health.pojo.vo.HealthMetricVO;
import com.example.health.service.HealthMetricService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/3/16 15:40
 * @description: 健康指标服务实现类
 */
@Service
public class HealthMetricServiceImpl implements HealthMetricService {
    /**
     * 自动装配持久层接口
     */
    @Resource
    HealthMetricMapper healthMetricMapper;

    /**
     * 新增健康指标
     *
     * @param healthMetricDTO 新增的健康指标信息
     * @return 新增操作响应结果
     */
    @Override
    public ApiResponse<String> save(HealthMetricDTO healthMetricDTO) {
        HealthMetricDO healthMetricDO = HealthMetricDO.builder()
                .healthMetricName(healthMetricDTO.getHealthMetricName())
                .healthMetricDescription(healthMetricDTO.getHealthMetricDescription())
                .healthMetricCover(healthMetricDTO.getHealthMetricCover())
                .healthMetricUnit(healthMetricDTO.getHealthMetricUnit())
                .healthMetricRange(healthMetricDTO.getHealthMetricRange()).build();
        healthMetricMapper.save(healthMetricDO);
        return ApiResponse.success("指标新增成功");
    }

    /**
     * 批量删除健康指标
     *
     * @param ids 待删除的健康指标ID集合
     * @return 删除操作响应结果
     */
    @Override
    public ApiResponse<String> deleteMetrics(List<Integer> ids) {
        healthMetricMapper.deleteMetrics(ids);
        return ApiResponse.success("指标删除成功");
    }

    /**
     * 更新健康指标信息
     *
     * @param healthMetricDTO 修改后的健康指标信息
     * @return 更新操作响应结果
     */
    @Override
    public ApiResponse<String> update(HealthMetricDTO healthMetricDTO) {
        HealthMetricDO healthMetricDO = healthMetricMapper.queryById(healthMetricDTO.getHealthMetricId());
        BeanUtils.copyProperties(healthMetricDTO, healthMetricDO);
        healthMetricMapper.update(healthMetricDO);
        return ApiResponse.success("指标信息更新成功");
    }

    /**
     * 根据指标名，分页+模糊查询健康指标
     *
     * @param healthMetricDTO 查询参数（部分指标名）
     * @return 查询结果 + 查询响应结果
     */
    @Override
    public ApiResponse<List<HealthMetricVO>> queryMetricsByName(HealthMetricDTO healthMetricDTO) {
        HealthMetricDO healthMetricDO = HealthMetricDO.builder()
                .healthMetricName(healthMetricDTO.getHealthMetricName()).build();
        List<HealthMetricVO> healthMetricVOList = healthMetricMapper.queryHealthMetrics(healthMetricDO,
                healthMetricDTO.getCurrent(),
                healthMetricDTO.getSize());
        Integer count = healthMetricMapper.queryHealthMetricsCount(healthMetricDO);
        return ApiResponse.success(healthMetricVOList, count);
    }
}
