package com.example.health.service;

import com.example.health.api.ApiResponse;
import com.example.health.mapper.HealthMetricMapper;
import com.example.health.pojo.dto.HealthRecordDTO;
import com.example.health.pojo.entity.HealthRecordDO;
import com.example.health.pojo.vo.HealthRecordVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/3/17 22:25
 * @description: 健康记录服务
 */
@Service
public interface HealthRecordService {
    /**
     * 新增若干条健康记录
     *
     * @param records 新增的若干条健康记录信息
     * @return 新增操作响应结果
     */
    ApiResponse<String> saveRecords(List<HealthRecordDTO> records);

    /**
     * 批量删除健康记录
     *
     * @param ids 待删除的健康记录信息
     * @return 删除操作响应结果
     */
    ApiResponse<String> deleteRecords(@Param(value = "ids") List<Long> ids);

    /**
     * 更新健康记录信息
     *
     * @param record 修改后的健康记录信息
     * @return 更新操作响应结果
     */
    ApiResponse<String> update(HealthRecordDTO record);

    /**
     * 分页查询健康记录
     *
     * @param record 查询参数（用户ID，指标ID） + 当前页、页大小
     * @return 满足查询条件的视图对象列表 + 查询操作响应结果
     */
    ApiResponse<List<HealthRecordVO>> queryRecords(HealthRecordDTO record);
}
