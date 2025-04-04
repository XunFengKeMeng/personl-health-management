package com.example.health.service.impl;

import com.example.health.api.ApiResponse;
import com.example.health.em.SubmissionStatusEnum;
import com.example.health.mapper.FormDataMapper;
import com.example.health.mapper.SubmissionMapper;
import com.example.health.pojo.dto.FormDataDTO;
import com.example.health.pojo.dto.SubmissionDTO;
import com.example.health.pojo.entity.FormDataDO;
import com.example.health.pojo.entity.SubmissionDO;
import com.example.health.pojo.vo.FormDataVO;
import com.example.health.pojo.vo.SubmissionVO;
import com.example.health.service.SubmissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huanghaiming
 * @date 2025/4/4 13:24
 * @description: 提交表单数据服务实现类
 */
@Service
public class SubmissionServiceImpl implements SubmissionService {
    /**
     * 表单提交记录持久层接口
     */
    @Resource
    private SubmissionMapper submissionMapper;

    /**
     * 提交表单表单项数据持久层接口
     */
    @Resource
    private FormDataMapper formDataMapper;


    /**
     * 新增表单提交记录（包含表单项数据）
     *
     * @param submissionDTO 新增的表单提交记录
     * @return 新增操作响应结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<String> save(SubmissionDTO submissionDTO) {
        // 表单提交记录新增
        SubmissionDO submissionDO = SubmissionDO.builder()
                .templateId(submissionDTO.getTemplateId())
                .userId(submissionDTO.getUserId())
                .status(SubmissionStatusEnum.PENDING.getCode())
                .submitTime(LocalDateTime.now())
                .build();
        submissionMapper.save(submissionDO);
        // 提交的表单项数据新增
        List<FormDataDO> formDataDOList = submissionDTO.getFormDataList()
                .stream().map(formDataDTO -> FormDataDO.builder()
                        .submissionId(submissionDO.getSubmissionId())
                        .itemId(formDataDTO.getItemId())
                        .value(formDataDTO.getValue())
                        .build()).collect(Collectors.toList());
        formDataMapper.batchSave(formDataDOList);
        return ApiResponse.success("表单提交成功");
    }

    /**
     * 删除指定表单提交记录及其表单项数据
     *
     * @param submissionDTO 待删除的表单提交记录信息
     * @return 删除操作响应结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<String> delete(SubmissionDTO submissionDTO) {
        submissionMapper.delete(submissionDTO.getSubmissionId());
        formDataMapper.deleteBySubmissionId(submissionDTO.getSubmissionId());
        return ApiResponse.success("表单提交记录删除成功");
    }

    /**
     * 更新表单提交记录（审核情况）及其表单项数据
     *
     * @param submissionDTO 修改后的表单提交信息
     * @return 更新操作响应结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<String> update(SubmissionDTO submissionDTO) {
        // 修改表单提交记录（审核状态）
        SubmissionDO submissionDO = SubmissionDO.builder()
                .submissionId(submissionDTO.getSubmissionId())
                .status(submissionDTO.getStatus())
                .build();
        submissionMapper.update(submissionDO);
        // 修改表单项数据
        List<FormDataDO> formDataDOList = submissionDTO.getFormDataList()
                .stream().map(formDataDTO -> FormDataDO.builder()
                        .submissionId(submissionDO.getSubmissionId())
                        .itemId(formDataDTO.getItemId())
                        .value(formDataDTO.getValue())
                        .build()).collect(Collectors.toList());
        formDataMapper.update(formDataDOList);
        return ApiResponse.success("表单数据修改成功");
    }

    /**
     * 模糊查询表单项记录（不包含表单项数据）
     *
     * @param submissionDTO 查询参数
     * @return 查询结果 + 查询操作响应结果
     */
    @Override
    public ApiResponse<List<SubmissionVO>> query(SubmissionDTO submissionDTO) {
        List<SubmissionVO> submissionVOList = submissionMapper.query(
                submissionDTO.getTemplateName(),
                submissionDTO.getUserName(),
                submissionDTO.getCurrent(),
                submissionDTO.getSize()
        );
        Integer count = submissionMapper.count(submissionDTO.getTemplateName(), submissionDTO.getUserName());
        return ApiResponse.success(submissionVOList, count);
    }

    /**
     * 查询特定提交表单数据（包含表单项数据）
     *
     * @param submissionDTO 查询参数（提交记录ID）
     * @return 查询结果 + 查询操作相应结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<SubmissionVO> queryBySubmissionId(SubmissionDTO submissionDTO) {
        // 获取提交表单数据（不包含表单项）
        SubmissionVO submissionVO = submissionMapper.queryById(submissionDTO.getSubmissionId());
        // 获取表单项数据
        List<FormDataVO> formDataVOList = formDataMapper.queryBySubmissionId(
                submissionVO.getSubmissionId(),
                submissionVO.getTemplateId());
        submissionVO.setFormDataList(formDataVOList);
        return ApiResponse.success(submissionVO);
    }
}
