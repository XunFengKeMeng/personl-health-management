package com.example.health.service;

import com.example.health.api.ApiResponse;
import com.example.health.pojo.dto.SubmissionDTO;
import com.example.health.pojo.vo.SubmissionVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/4/4 12:01
 * @description: 表单提交记录服务
 */
@Service
public interface SubmissionService {
    /**
     * 新增表单提交记录（包含表单项数据）
     *
     * @param submissionDTO 新增的表单提交记录
     * @return 新增操作响应结果
     */
    ApiResponse<String> save(SubmissionDTO submissionDTO);

    /**
     * 删除指定表单提交记录及其表单项数据
     * @param submissionDTO 待删除的表单提交记录信息
     * @return 删除操作响应结果
     */
    ApiResponse<String> delete(SubmissionDTO submissionDTO);

    /**
     * 更新表单提交记录（审核情况）及其表单项数据
     * @param submissionDTO 修改后的表单提交信息
     * @return 更新操作响应结果
     */
    ApiResponse<String> update(SubmissionDTO submissionDTO);

    /**
     * 模糊查询表单项记录（不包含表单项数据）
     *
     * @param submissionDTO 查询参数
     * @return 查询结果 + 查询操作响应结果
     */
    ApiResponse<List<SubmissionVO>> query(SubmissionDTO submissionDTO);

    /**
     * 查询特定提交表单数据（包含表单项数据）
     *
     * @param submissionDTO 查询参数（提交记录ID）
     * @return 查询结果 + 查询操作相应结果
     */
    ApiResponse<SubmissionVO> queryBySubmissionId(SubmissionDTO submissionDTO );
}
