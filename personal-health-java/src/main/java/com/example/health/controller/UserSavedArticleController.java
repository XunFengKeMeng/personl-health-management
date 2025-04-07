package com.example.health.controller;

import com.example.health.api.ApiResponse;
import com.example.health.pojo.dto.UserSavedArticleDTO;
import com.example.health.pojo.vo.UserSavedArticleVO;
import com.example.health.service.UserSavedArticleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/3/16 11:19
 * @description: 用户收藏健康资讯Controller
 */
@RestController
@RequestMapping("/articleSaved")
public class UserSavedArticleController {
    /**
     * 自动装配服务
     */
    @Resource
    private UserSavedArticleService userSavedArticleService;

    /**
     * 新增收藏记录
     *
     * @param userSavedArticleDTO 新增的收藏记录信息
     * @return 新增操作响应结果
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public ApiResponse<String> save(@RequestBody UserSavedArticleDTO userSavedArticleDTO) {
        return userSavedArticleService.save(userSavedArticleDTO);
    }

    /**
     * 删除收藏记录（1~若干）
     *
     * @param ids 待删除的记录ID集合
     * @return 删除操作响应结果
     */
    @PostMapping(value = "/delete")
    @ResponseBody
    public ApiResponse<String> delete(@RequestBody List<Integer> ids) {
        return userSavedArticleService.deleteSavedArticles(ids);
    }

    /**
     * 删除指定收藏记录
     *
     * @param userSavedArticleDTO 删除记录使用参数（用户ID、资讯ID）
     * @return 删除操作响应结果
     */
    @PostMapping(value = "/deleteOne")
    @ResponseBody
    public ApiResponse<String> deleteOne(@RequestBody UserSavedArticleDTO userSavedArticleDTO) {
        return userSavedArticleService.deleteOneSaved(userSavedArticleDTO);
    }

    /**
     * 分页查询收藏记录（根据用户ID/健康资讯ID）
     *
     * @param userSavedArticleDTO 包含用户ID/健康资讯ID的查询参数
     * @return 查询结果 + 查询操作响应结果
     */
    @PostMapping(value = "/query")
    @ResponseBody
    public ApiResponse<List<UserSavedArticleVO>> queryUserSavedArticles(@RequestBody UserSavedArticleDTO userSavedArticleDTO) {
        return userSavedArticleService.querySavedArticles(userSavedArticleDTO);
    }
}
