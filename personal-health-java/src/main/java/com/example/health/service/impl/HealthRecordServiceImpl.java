package com.example.health.service.impl;

import com.example.health.api.ApiResponse;
import com.example.health.mapper.HealthRecordMapper;
import com.example.health.pojo.dto.HealthRecordDTO;
import com.example.health.pojo.entity.HealthRecordDO;
import com.example.health.pojo.vo.HealthRecordVO;
import com.example.health.service.HealthRecordService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/3/17 22:33
 * @description: 健康记录服务实现类
 */
@Service
public class HealthRecordServiceImpl implements HealthRecordService {
    /**
     * 注入持久层接口
     */
    @Resource
    private HealthRecordMapper healthRecordMapper;

    /**
     * 新增若干条健康记录
     *
     * @param records 新增的若干条健康记录信息
     * @return 新增操作响应结果
     */
    @Override
    public ApiResponse<String> saveRecords(List<HealthRecordDTO> records) {
        List<HealthRecordDO> recordDOList = new ArrayList<HealthRecordDO>();
        for (HealthRecordDTO record : records) {
            HealthRecordDO recordDO = HealthRecordDO.builder().recordCreateTime(LocalDateTime.now()).build();
            BeanUtils.copyProperties(record, recordDO);
            recordDOList.add(recordDO);
        }
        healthRecordMapper.saveRecords(recordDOList);
        return ApiResponse.success("新增成功");
    }

    /**
     * 批量删除健康记录
     *
     * @param ids 待删除的健康记录信息
     * @return 删除操作响应结果
     */
    @Override
    public ApiResponse<String> deleteRecords(List<Long> ids) {
        healthRecordMapper.deleteRecords(ids);
        return ApiResponse.success("删除成功");
    }

    /**
     * 更新健康记录信息
     *
     * @param record 修改后的健康记录信息
     * @return 更新操作响应结果
     */
    @Override
    public ApiResponse<String> update(HealthRecordDTO record) {
        HealthRecordDO recordDO = new HealthRecordDO();
        BeanUtils.copyProperties(record, recordDO);
        healthRecordMapper.update(recordDO);
        return ApiResponse.success("修改成功");
    }

    /**
     * 分页查询健康记录
     *
     * @param record 查询参数（用户ID，指标ID） + 当前页、页大小
     * @return 满足查询条件的视图对象列表 + 查询操作响应结果
     */
    @Override
    public ApiResponse<List<HealthRecordVO>> queryRecords(HealthRecordDTO record) {
        HealthRecordDO recordDO = new HealthRecordDO();
        BeanUtils.copyProperties(record, recordDO);
        List<HealthRecordVO> healthRecordVOList = healthRecordMapper.queryRecords(
                recordDO,
                record.getCurrent(),
                record.getSize());
        Integer count = healthRecordMapper.queryRecordsCount(recordDO);
        return ApiResponse.success(healthRecordVOList, count);
    }
}
