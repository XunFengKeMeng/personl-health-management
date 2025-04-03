package com.example.health.controller;

import com.example.health.api.ApiResponse;
import com.example.health.pojo.dto.FormItemDTO;
import com.example.health.pojo.vo.FormItemVO;
import com.example.health.service.FormItemService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/4/2 14:12
 * @description: 表单项Controller
 */
@RestController
@RequestMapping("/formItem")
public class FormItemController {
    /**
     * 自动装配服务
     */
    @Resource
    private FormItemService formItemService;

    /**
     * 新增表单项
     *
     * @param formItemDTO 新增表单项信息
     * @return 新增操作响应结果
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public ApiResponse<String> saveFormItem(@RequestBody FormItemDTO formItemDTO) {
        return formItemService.save(formItemDTO);
    }

    /**
     * 删除表单项
     *
     * @param ids 待删除的表单项ID集合
     * @return 删除操作响应结果
     */
    @PostMapping(value = "/deleteItems")
    @ResponseBody
    public ApiResponse<String> deleteItems(@RequestBody List<Integer> ids) {
        return formItemService.deleteItems(ids);
    }

    /**
     * 更新表单项信息
     *
     * @param formItemDTO 修改后的表单项信息
     * @return 修改操作相应信息
     */
    @PostMapping(value = "/update")
    @ResponseBody
    public ApiResponse<String> updateFormItem(@RequestBody FormItemDTO formItemDTO) {
        return formItemService.update(formItemDTO);
    }

    /**
     * 模糊查询表单项信息
     *
     * @param formItemDTO 查询参数
     * @return 查询结果 + 查询操作响应结果
     */
    @PostMapping(value = "/queryItems")
    @ResponseBody
    public ApiResponse<List<FormItemVO>> queryItems(@RequestBody FormItemDTO formItemDTO) {
        return formItemService.queryItems(formItemDTO);
    }
}
