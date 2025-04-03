package com.example.health.service;

import com.example.health.api.ApiResponse;
import com.example.health.pojo.dto.FormTemplateDTO;
import com.example.health.pojo.vo.FormTemplateDetailVO;
import com.example.health.pojo.vo.FormTemplateVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/4/2 19:00
 * @description: 表单模板服务
 */
@Service
public interface FormTemplateService {
    /**
     * 新增表单模板（包含表单项）
     *
     * @param formTemplateDTO 新增的表单模板信息
     * @return 新增操作响应结果
     */
    ApiResponse<String> save(FormTemplateDTO formTemplateDTO);

    /**
     * 删除指定表单模板
     *
     * @param templateId 待删除的表单模板ID
     * @return 删除操作响应结果
     */
    ApiResponse<String> deleteByTemplateId(Integer templateId);

    /**
     * 更新表单模板数据（包含表单项）
     *
     * @param formTemplateDTO  修改后的表单模板数据
     * @return 更新操作响应结果
     */
    ApiResponse<String> update(FormTemplateDTO formTemplateDTO);

    /**
     * 模糊查询表单模板数据
     *
     * @param formTemplateDTO 查询参数
     * @return 查询结果 + 查询操作响应结果
     */
    ApiResponse<List<FormTemplateVO>> query(FormTemplateDTO formTemplateDTO);

    /**
     * 根据表单模板ID，获取表单详细数据视图对象
     *
     * @param templateId 表单模板ID
     * @return 查询结果 + 查询响应结果
     */
    ApiResponse<FormTemplateDetailVO> queryByTemplateId(Integer templateId);
}
