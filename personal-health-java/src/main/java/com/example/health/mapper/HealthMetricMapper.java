package com.example.health.mapper;

import com.example.health.pojo.entity.HealthMetricDO;
import com.example.health.pojo.vo.HealthMetricVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/3/5 09:48
 * @description: 健康指标持久层接口
 */
@Mapper
public interface HealthMetricMapper {
    /**
     * 新增健康指标
     *
     * @param healthMetricDO 新增的健康指标信息
     */
    void save(HealthMetricDO healthMetricDO);

    /**
     * 删除健康指标
     *
     * @param ids 待删除的健康指标信息
     */
    void deleteMetrics(@Param(value = "ids") List<Integer> ids);

    /**
     * 更新健康指标信息
     *
     * @param healthMetricDO 修改后的健康指标信息
     */
    void update(HealthMetricDO healthMetricDO);

    /**
     * 根据指标ID查询指标信息
     *
     * @param metricId 指标ID
     * @return 查询到的指标信息
     */
    HealthMetricDO queryById(@Param(value = "metricId") Integer metricId);

    /**
     * 分页+模糊查询健康指标
     *
     * @param healthMetricDO 查询参数
     * @param current 当前页
     * @param size 页大小
     * @return 查询结果
     */
    List<HealthMetricVO> queryHealthMetrics(
            @Param("metric") HealthMetricDO healthMetricDO,
            @Param("current") Integer current,
            @Param("size") Integer size);

    /**
     * 满足分页+模糊查询的健康指标数
     *
     * @param healthMetricDO 查询参数
     * @return 满足查询参数的指标数
     */
    Integer queryHealthMetricsCount(HealthMetricDO healthMetricDO);
}
