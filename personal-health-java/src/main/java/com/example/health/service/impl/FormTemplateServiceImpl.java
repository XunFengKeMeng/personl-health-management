package com.example.health.service.impl;

import com.example.health.api.ApiResponse;
import com.example.health.mapper.FormTemplateMapper;
import com.example.health.mapper.TemplateItemRelationMapper;
import com.example.health.pojo.dto.FormTemplateDTO;
import com.example.health.pojo.entity.FormTemplateDO;
import com.example.health.pojo.entity.TemplateItemRelationDO;
import com.example.health.pojo.vo.FormTemplateDetailVO;
import com.example.health.pojo.vo.FormTemplateVO;
import com.example.health.pojo.vo.TemplateItemRelationVO;
import com.example.health.service.FormTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huanghaiming
 * @date 2025/4/2 19:02
 * @description: 表单模板服务实现类
 */
@Service
public class FormTemplateServiceImpl implements FormTemplateService {
    /**
     * 表单模板持久化接口自动装配
     */
    @Resource
    private FormTemplateMapper formTemplateMapper;

    /**
     * 表单模板关联项持久化接口自动装配
     */
    @Resource
    private TemplateItemRelationMapper templateItemRelationMapper;

    /**
     * 新增表单模板（包含表单项）
     *
     * @param formTemplateDTO 新增的表单模板信息
     * @return 新增操作响应结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<String> save(FormTemplateDTO formTemplateDTO) {
        FormTemplateDO formTemplateDO = FormTemplateDO.builder()
                .templateName(formTemplateDTO.getTemplateName())
                .creatorId(formTemplateDTO.getCreatorId())
                .active(formTemplateDTO.getActive())
                .createTime(LocalDateTime.now())
                .build();
        formTemplateMapper.save(formTemplateDO);
        // 新增表单模板关联的所有表单项
        List<TemplateItemRelationDO> templateItemRelationDOList = formTemplateDTO.getItemList()
                .stream().map(itemDTO -> TemplateItemRelationDO.builder()
                        .templateId(formTemplateDO.getTemplateId())
                        .itemId(itemDTO.getItemId())
                        .sortOrder(itemDTO.getSortOrder())
                        .build()).collect(Collectors.toList());
        templateItemRelationMapper.batchSave(templateItemRelationDOList);

        return ApiResponse.success("表单模板及其相关项新增成功");
    }

    /**
     * 删除指定表单模板
     *
     * @param templateId 待删除的表单模板ID
     * @return 删除操作响应结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<String> deleteByTemplateId(Integer templateId) {
        templateItemRelationMapper.delete(templateId);
        formTemplateMapper.deleteByTemplateId(templateId);
        return ApiResponse.success("表单模板及其相关项删除成功");
    }

    /**
     * 更新表单模板数据（包含表单项）
     *
     * @param formTemplateDTO 修改后的表单模板数据
     * @return 更新操作响应结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<String> update(FormTemplateDTO formTemplateDTO) {
        // 修改表单模板数据
        FormTemplateDO formTemplateDO = FormTemplateDO.builder()
                .templateId(formTemplateDTO.getTemplateId())
                .templateName(formTemplateDTO.getTemplateName())
                .active(formTemplateDTO.getActive())
                .build();
        formTemplateMapper.update(formTemplateDO);
        // 删除后再新增表单模板关联的所有表单项
        List<TemplateItemRelationDO> templateItemRelationDOList = formTemplateDTO.getItemList()
                .stream().map(itemDTO -> TemplateItemRelationDO.builder()
                        .templateId(formTemplateDTO.getTemplateId())
                        .itemId(itemDTO.getItemId())
                        .sortOrder(itemDTO.getSortOrder())
                        .build()).collect(Collectors.toList());
        templateItemRelationMapper.delete(formTemplateDTO.getTemplateId());
        templateItemRelationMapper.batchSave(templateItemRelationDOList);
        return ApiResponse.success("表单模板及其相关项更新成功");
    }

    /**
     * 模糊查询表单模板数据（不包含关联项）
     *
     * @param formTemplateDTO 查询参数
     * @return 查询结果 + 查询操作响应结果
     */
    @Override
    public ApiResponse<List<FormTemplateVO>> query(FormTemplateDTO formTemplateDTO) {
        // 获取查询参数
        FormTemplateDO formTemplateDO = FormTemplateDO.builder()
                .templateName(formTemplateDTO.getTemplateName())
                .active(formTemplateDTO.getActive())
                .build();
        // 调用查询方法
        List<FormTemplateVO> formTemplateVOList = formTemplateMapper.query(formTemplateDO,
                formTemplateDTO.getCurrent(),
                formTemplateDTO.getSize());
        Integer count = formTemplateMapper.count(formTemplateDO);
        return ApiResponse.success(formTemplateVOList, count);
    }

    /**
     * 根据表单模板ID，获取表单详细数据视图对象
     *
     * @param templateId 表单模板ID
     * @return 查询结果 + 查询响应结果
     */
    @Override
    public ApiResponse<FormTemplateDetailVO> queryByTemplateId(Integer templateId) {
        // 获取除表单项外的表单模板信息
        FormTemplateVO formTemplateVO = formTemplateMapper.queryByTemplateId(templateId);
        // 获取所有表单项
        List<TemplateItemRelationVO> templateItemRelationVOList = templateItemRelationMapper.query(templateId);
        // 查询结果设置
        FormTemplateDetailVO formTemplateDetailVO = FormTemplateDetailVO.builder()
                .templateId(formTemplateVO.getTemplateId())
                .templateName(formTemplateVO.getTemplateName())
                .creatorName(formTemplateVO.getCreatorName())
                .active(formTemplateVO.getActive())
                .createTime(formTemplateVO.getCreateTime())
                .itemList(templateItemRelationVOList)
                .build();
        return ApiResponse.success(formTemplateDetailVO);
    }
}
