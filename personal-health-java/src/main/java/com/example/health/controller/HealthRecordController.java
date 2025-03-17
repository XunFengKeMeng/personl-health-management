package com.example.health.controller;

import com.example.health.api.ApiResponse;
import com.example.health.pojo.dto.HealthRecordDTO;
import com.example.health.pojo.vo.HealthRecordVO;
import com.example.health.service.HealthRecordService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/3/17 22:41
 * @description: 健康记录Controller
 */
@RestController
@RequestMapping("/record")
public class HealthRecordController {
    /**
     * 注入服务
     */
    @Resource
    private HealthRecordService healthRecordService;

    /**
     * 新增若干健康记录
     *
     * @param records 新增的健康记录列表
     * @return 新增操作响应结果
     */
    @RequestMapping(value = "/saveRecords")
    @ResponseBody
    public ApiResponse<String> saveRecords(@RequestBody List<HealthRecordDTO> records) {
        return healthRecordService.saveRecords(records);
    }

    /**
     * 批量删除健康记录
     *
     * @param ids 待删除的健康记录ID集合
     * @return 删除操作响应结果
     */
    @RequestMapping(value = "/deleteRecords")
    @ResponseBody
    public ApiResponse<String> deleteRecords(@RequestBody List<Long> ids) {
        return healthRecordService.deleteRecords(ids);
    }

    /**
     * 更新健康记录信息
     *
     * @param record 修改后的健康记录信息
     * @return 更新操作响应结果
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public ApiResponse<String> update(@RequestBody HealthRecordDTO record) {
        return healthRecordService.update(record);
    }

    /**
     * 分页查询健康记录信息
     *
     * @param record 查询参数 + 页参数
     * @return 满足条件的健康记录信息列表 + 查询操作响应结果
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public ApiResponse<List<HealthRecordVO>> query(@RequestBody HealthRecordDTO record) {
        return healthRecordService.queryRecords(record);
    }
}
