package com.example.health.controller;

import com.example.health.api.ApiResponse;
import com.example.health.pojo.dto.HealthMetricDTO;
import com.example.health.pojo.vo.HealthMetricVO;
import com.example.health.service.HealthMetricService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/3/16 16:10
 * @description: 健康指标Controller
 */
@RestController
@RequestMapping("/metric")
public class HealthMetricController {
    /**
     * 自动装配服务
     */
    @Resource
    private HealthMetricService healthMetricService;

    /**
     * 新增健康指标
     *
     * @param healthMetricDTO 新增的健康指标信息
     * @return 新增操作响应结果
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public ApiResponse<String> saveMetric(@RequestBody HealthMetricDTO healthMetricDTO) {
        return healthMetricService.save(healthMetricDTO);
    }

    /**
     * 批量删除健康指标
     *
     * @param ids 待删除的健康指标ID结合
     * @return 删除操作响应结果
     */
    @PostMapping(value = "/deleteMetrics")
    @ResponseBody
    public ApiResponse<String> deleteMetrics(@RequestBody List<Integer> ids) {
        return healthMetricService.deleteMetrics(ids);
    }

    /**
     * 更新健康指标信息
     *
     * @param healthMetricDTO 修改后的健康指标信息
     * @return 更新操作响应结果
     */
    @PostMapping(value = "/update")
    @ResponseBody
    public ApiResponse<String> updateMetric(@RequestBody HealthMetricDTO healthMetricDTO) {
        return healthMetricService.update(healthMetricDTO);
    }

    /**
     * （根据指标名）模糊查询指标
     * @param healthMetricDTO 包含部分指标名的健康指标信息
     * @return 满足查询条件的健康指标列表 + 查询操作响应结果
     */
    @PostMapping(value = "/queryMetrics")
    @ResponseBody
    public ApiResponse<List<HealthMetricVO>> queryMetrics(@RequestBody HealthMetricDTO healthMetricDTO) {
        return healthMetricService.queryMetricsByName(healthMetricDTO);
    }
}
