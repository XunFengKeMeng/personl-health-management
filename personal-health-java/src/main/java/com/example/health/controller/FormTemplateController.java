package com.example.health.controller;

import com.example.health.api.ApiResponse;
import com.example.health.pojo.dto.FormTemplateDTO;
import com.example.health.pojo.vo.FormTemplateDetailVO;
import com.example.health.pojo.vo.FormTemplateVO;
import com.example.health.service.FormTemplateService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/4/3 13:57
 * @description: 表单模板Controller
 */
@RestController
@RequestMapping("/formTemplate")
public class FormTemplateController {
    /**
     * 表单模板服务自动装配
     */
    @Resource
    private FormTemplateService formTemplateService;

    /**
     * 新增表单模板（包含关联项）
     *
     * @param formTemplateDTO 新增的表单模板信息
     * @return 新增操作响应结果
     */
    @PostMapping(value = "/save")
    @ResponseBody
    @RequiresRoles("admin")
    public ApiResponse<String> save(@RequestBody FormTemplateDTO formTemplateDTO) {
        return formTemplateService.save(formTemplateDTO);
    }

    /**
     * 删除表单模板（包含表单项）
     *
     * @param formTemplateDTO 待删除的表单模板信息（模板ID）
     * @return 删除操作响应结果
     */
    @PostMapping(value = "/delete")
    @ResponseBody
    @RequiresRoles("admin")
    public ApiResponse<String> delete(@RequestBody FormTemplateDTO formTemplateDTO) {
        return formTemplateService.deleteByTemplateId(formTemplateDTO.getTemplateId());
    }

    /**
     * 更新表单模板数据（包含表单项）
     *
     * @param formTemplateDTO 修改后的表单模板数据
     * @return 更新操作响应结果
     */
    @PostMapping(value = "/update")
    @ResponseBody
    @RequiresRoles("admin")
    public ApiResponse<String> update(@RequestBody FormTemplateDTO formTemplateDTO) {
        return formTemplateService.update(formTemplateDTO);
    }

    /**
     * 模糊查询表单模板数据（不包含关联项）
     *
     * @param formTemplateDTO 查询参数
     * @return 查询结果 + 查询操作响应结果
     */
    @PostMapping(value = "/query")
    @ResponseBody
    @RequiresRoles("user")
    public ApiResponse<List<FormTemplateVO>> query(@RequestBody FormTemplateDTO formTemplateDTO) {
        return formTemplateService.query(formTemplateDTO);
    }

    /**
     * 查询表单模板数据（包含关联项）
     *
     * @param formTemplateDTO 查询参数
     * @return 查询结果 + 查询操作响应结果
     */
    @PostMapping(value = "/queryDetail")
    @ResponseBody
    @RequiresRoles("user")
    public ApiResponse<FormTemplateDetailVO> queryDetail(@RequestBody FormTemplateDTO formTemplateDTO) {
        return formTemplateService.queryByTemplateId(formTemplateDTO.getTemplateId());
    }
}
