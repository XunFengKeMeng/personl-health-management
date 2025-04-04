package com.example.health.controller;

import com.example.health.api.ApiResponse;
import com.example.health.pojo.dto.SubmissionDTO;
import com.example.health.pojo.vo.SubmissionVO;
import com.example.health.service.SubmissionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/4/4 14:00
 * @description: 表单提交Controller
 */
@RestController
@RequestMapping("/formSubmission")
public class SubmissionController {
    /**
     * 表单提交服务自动装配
     */
    @Resource
    private SubmissionService submissionService;

    /**
     * 新增表单提交信息（用户）
     *
     * @param submissionDTO 表单提交数据
     * @return 新增操作响应结果
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public ApiResponse<String> save(@RequestBody SubmissionDTO submissionDTO) {
        return submissionService.save(submissionDTO);
    }

    /**
     * 删除表单提交数据（单条）
     *
     * @param submissionDTO 待删除的表单数据（提交记录ID）
     * @return 删除操作响应结果
     */
    @PostMapping(value = "/delete")
    @ResponseBody
    public ApiResponse<String> delete(@RequestBody SubmissionDTO submissionDTO) {
        return submissionService.delete(submissionDTO);
    }

    /**
     * 更新表单提交数据
     *
     * @param submissionDTO 修改后的表单提交数据
     * @return 更新操作响应结果
     */
    @PostMapping(value = "/update")
    @ResponseBody
    public ApiResponse<String> update(@RequestBody SubmissionDTO submissionDTO) {
        return submissionService.update(submissionDTO);
    }

    /**
     * 模糊查询表单提交记录
     *
     * @param submissionDTO 部分用户名/表单模板名
     * @return 查询结果 + 查询操作响应结果
     */
    @PostMapping(value = "/query")
    @ResponseBody
    public ApiResponse<List<SubmissionVO>> query(@RequestBody SubmissionDTO submissionDTO) {
        return submissionService.query(submissionDTO);
    }

    /**
     * 查询提交表单的所有数据
     *
     * @param submissionDTO 提交记录ID
     * @return 查询结果 + 查询操作响应结果
     */
    @PostMapping(value = "/queryDetail")
    @ResponseBody
    public ApiResponse<SubmissionVO> queryDetail(@RequestBody SubmissionDTO submissionDTO) {
        return submissionService.queryBySubmissionId(submissionDTO);
    }
}
