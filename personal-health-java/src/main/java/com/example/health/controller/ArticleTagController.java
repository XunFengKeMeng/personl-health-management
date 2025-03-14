package com.example.health.controller;

import com.example.health.api.ApiResponse;
import com.example.health.pojo.dto.ArticleTagDTO;
import com.example.health.pojo.vo.ArticleTagVO;
import com.example.health.service.ArticleTagService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/3/14 13:49
 * @description: 健康资讯标签Controller
 */
@RestController
@RequestMapping("/tag")
public class ArticleTagController {

    /**
     * 自动装配服务
     */
    @Resource
    private ArticleTagService articleTagService;

    /**
     * 新增标签
     *
     * @param articleTagDTO 标签信息
     * @return 新增操作响应结果
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public ApiResponse<String> save(@RequestBody ArticleTagDTO articleTagDTO) {
        return articleTagService.saveArticleTag(articleTagDTO);
    }

    /**
     * 批量删除标签
     *
     * @param ids 待删除的标签集合
     * @return 删除操作响应结果
     */
    @PostMapping(value = "/deleteTags")
    @ResponseBody
    public ApiResponse<String> deleteArticleTags(@RequestBody List<Integer> ids) {
        return articleTagService.deleteArticleTags(ids);
    }

    /**
     * 更新标签信息
     *
     * @param articleTagDTO 修改后的标签信息
     * @return 修改操作响应结果
     */
    @PostMapping(value = "/update")
    @ResponseBody
    public ApiResponse<String> update(@RequestBody ArticleTagDTO articleTagDTO) {
        return articleTagService.updateArticleTag(articleTagDTO);
    }

    /**
     * 分页+模糊查询标签信息
     *
     * @param articleTagDTO 查询参数
     * @return 查询操作响应结果 + 满足查询参数的标签信息列表
     */
    @PostMapping(value = "/queryTags")
    @ResponseBody
    public ApiResponse<List<ArticleTagVO>> queryArticleTags(@RequestBody ArticleTagDTO articleTagDTO) {
        return articleTagService.queryArticleTags(articleTagDTO);
    }
}
