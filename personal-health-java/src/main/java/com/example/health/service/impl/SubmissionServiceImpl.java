package com.example.health.service.impl;

import com.example.health.api.ApiResponse;
import com.example.health.em.NoticeTypeEnum;
import com.example.health.em.ReadStatusEnum;
import com.example.health.em.SubmissionStatusEnum;
import com.example.health.mapper.FormDataMapper;
import com.example.health.mapper.NoticeMapper;
import com.example.health.mapper.SubmissionMapper;
import com.example.health.mapper.UserMapper;
import com.example.health.pojo.dto.SubmissionDTO;
import com.example.health.pojo.entity.FormDataDO;
import com.example.health.pojo.entity.NoticeDO;
import com.example.health.pojo.entity.SubmissionDO;
import com.example.health.pojo.entity.UserDO;
import com.example.health.pojo.vo.FormDataVO;
import com.example.health.pojo.vo.SubmissionVO;
import com.example.health.service.SubmissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
     * 校验健康表单项并发送通知
     */
    @Resource
    private NoticeMapper noticeMapper;

    /**
     * 审核时，对用户健康状态进行赋值
     */
    @Resource
    private UserMapper userMapper;

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
     * 更新表单提交记录（审核情况）及其表单项数据并发布通知（管理员）
     * 如果是体检结果表，还会修改用户的健康状态
     *
     * @param submissionDTO 修改后的表单提交信息
     * @return 更新操作响应结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<String> updateAndNotice(SubmissionDTO submissionDTO) {
        // 修改表单提交记录（审核状态）
        SubmissionDO submissionDO = SubmissionDO.builder()
                .submissionId(submissionDTO.getSubmissionId())
                .status(submissionDTO.getStatus())
                .build();
        submissionMapper.update(submissionDO);
        // 修改用户的健康状态（如果表单ID为1，即体检结果表时）
        if(submissionDTO.getTemplateId() == 1) {
            UserDO userDO = UserDO.builder()
                    .userId(submissionDTO.getUserId())
                    .healthStatus(submissionDTO.getHealthStatus()).build();
            userMapper.updateUser(userDO);
        }
        // 通知消息体
        String noticeMsg;
        SubmissionVO submission = submissionMapper.queryById(submissionDTO.getSubmissionId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(Objects.equals(submissionDTO.getStatus(), SubmissionStatusEnum.APPROVED.getCode())){
            // 获取
            List<String> itemNameList = submissionMapper.queryHealthMetricViolations(submissionDTO.getSubmissionId());

            noticeMsg = Optional.ofNullable(itemNameList)
                    .filter(list -> !list.isEmpty())
                    .map(list -> String.join(",", list))
                    .map(healthMetricViolations ->
                            String.format(
                                    "亲爱的%s:%n  您于%s提交的%s已经审核通过。%n%n经系统检测，您的%s数值超出正常范围。为确保您的健康安全，建议：%n" +
                                            "1. 核对填报数据是否准确%n" +
                                            "2. 如数据无误，请及时咨询专业医师%n" +
                                            "3. 关注相关健康指标变化%n%n" +
                                            "祝您健康平安！",
                                    submission.getUserName(),
                                    submission.getSubmitTime().format(formatter),
                                    submission.getTemplateName(),
                                    healthMetricViolations
                            )
                    )
                    .orElseGet(() ->
                            String.format(
                                    "亲爱的%s:%n  您于%s提交的%s已经审核通过。",
                                    submission.getUserName(),
                                    submission.getSubmitTime().format(formatter),
                                    submission.getTemplateName()
                            )
                    );
        } else {
            noticeMsg = String.format(
                    "亲爱的%s:%n  您于%s提交的%s审核未通过，请正确填写表单。",
                    submission.getUserName(),
                    submission.getSubmitTime().format(formatter),
                    submission.getTemplateName());
        }
        // 发送通知
        NoticeDO noticeDO = NoticeDO.builder()
                .receiverId(submission.getUserId())
                .read(ReadStatusEnum.UNREAD.getReadStatus())
                .noticeCreateTime(LocalDateTime.now())
                .noticeType(NoticeTypeEnum.FORM_ALERT.getNoticeTypeCode())
                .noticeContent(noticeMsg).build();

        noticeMapper.insertNotice(noticeDO);

        return ApiResponse.success("表单审核成功");
    }

    /**
     * 更新被退回的提交表单数据（用户）
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
        // 修改提交表单项数据
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
        SubmissionDO submissionDO = SubmissionDO.builder()
                .templateName(submissionDTO.getTemplateName())
                .userId(submissionDTO.getUserId())
                .userName(submissionDTO.getUserName())
                .build();
        List<SubmissionVO> submissionVOList = submissionMapper.query(
                submissionDO,
                submissionDTO.getCurrent(),
                submissionDTO.getSize()
        );
        Integer count = submissionMapper.count(submissionDO);
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
