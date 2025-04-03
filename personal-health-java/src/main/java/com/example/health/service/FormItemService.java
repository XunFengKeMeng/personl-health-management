package com.example.health.service;

import com.example.health.api.ApiResponse;
import com.example.health.pojo.dto.FormItemDTO;
import com.example.health.pojo.vo.FormItemVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/4/2 13:56
 * @description: 表单项服务
 */
@Service
public interface FormItemService {
    /**
     * 新增表单项
     *
     * @param formItemDTO 新增表单项信息
     * @return 新增操作响应结果
     */
    ApiResponse<String> save(FormItemDTO formItemDTO);

    /**
     * 批量删除表单项
     *
     * @param ids 待删除的表单项ID集合
     * @return 删除操作响应结果
     */
    ApiResponse<String> deleteItems(List<Integer> ids);

    /**
     * 修改表单项信息
     *
     * @param formItemDTO 修改后的表单项信息
     * @return 修改操作响应结果
     */
    ApiResponse<String> update(FormItemDTO formItemDTO);

    /**
     * 模糊查询表单项
     *
     * @param formItemDTO 查询参数
     * @return 查询结果 + 查询操作响应结果
     */
    ApiResponse<List<FormItemVO>> queryItems(FormItemDTO formItemDTO);
}
