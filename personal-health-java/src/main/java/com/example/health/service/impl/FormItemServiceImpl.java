package com.example.health.service.impl;

import com.example.health.api.ApiResponse;
import com.example.health.mapper.FormItemMapper;
import com.example.health.pojo.dto.FormItemDTO;
import com.example.health.pojo.entity.FormItemDO;
import com.example.health.pojo.vo.FormItemVO;
import com.example.health.service.FormItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/4/2 14:01
 * @description: 表单项服务实现类
 */
@Service
public class FormItemServiceImpl implements FormItemService {
    /**
     * 自动装配持久层接口
     */
    @Resource
    private FormItemMapper formItemMapper;

    /**
     * 新增表单项
     *
     * @param formItemDTO 新增表单项信息
     * @return 新增操作响应结果
     */
    @Override
    public ApiResponse<String> save(FormItemDTO formItemDTO) {
        FormItemDO formItemDO = FormItemDO.builder()
                .itemName(formItemDTO.getItemName())
                .healthMetricId(formItemDTO.getHealthMetricId())
                .placeholder(formItemDTO.getPlaceholder())
                .required(formItemDTO.getRequired()).build();
        formItemMapper.saveFormItem(formItemDO);
        return ApiResponse.success("表单项新增成功");
    }

    /**
     * 批量删除表单项
     *
     * @param ids 待删除的表单项ID集合
     * @return 删除操作响应结果
     */
    @Override
    public ApiResponse<String> deleteItems(List<Integer> ids) {
        formItemMapper.deleteFormItems(ids);
        return ApiResponse.success("表单项删除成功");
    }

    /**
     * 修改表单项信息
     *
     * @param formItemDTO 修改后的表单项信息
     * @return 修改操作响应结果
     */
    @Override
    public ApiResponse<String> update(FormItemDTO formItemDTO) {
        FormItemDO formItemDO = FormItemDO.builder()
                .itemId(formItemDTO.getItemId())
                .itemName(formItemDTO.getItemName())
                .healthMetricId(formItemDTO.getHealthMetricId())
                .placeholder(formItemDTO.getPlaceholder())
                .required(formItemDTO.getRequired()).build();
        formItemMapper.updateFormItem(formItemDO);
        return ApiResponse.success("表单项信息更新成功");
    }

    /**
     * 模糊查询表单项
     *
     * @param formItemDTO 查询参数（表单项名部分）
     * @return 查询结果 + 查询操作响应结果
     */
    @Override
    public ApiResponse<List<FormItemVO>> queryItems(FormItemDTO formItemDTO) {
        FormItemDO formItemDO = FormItemDO.builder().itemName(formItemDTO.getItemName()).build();
        List<FormItemVO> formItemVOList = formItemMapper.queryFormItems(formItemDO,
                formItemDTO.getCurrent(), formItemDTO.getSize());
        Integer count = formItemMapper.queryFormItemsCount(formItemDO);
        return ApiResponse.success(formItemVOList, count);
    }
}
