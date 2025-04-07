package com.example.health.controller;

import com.example.health.api.ApiResponse;
import com.example.health.pojo.dto.HealthArticleDTO;
import com.example.health.pojo.dto.query.extend.HealthArticleQueryDTO;
import com.example.health.pojo.vo.ArticleTagStatisticsVO;
import com.example.health.pojo.vo.ArticleTrendVO;
import com.example.health.pojo.vo.HealthArticleVO;
import com.example.health.service.HealthArticleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/3/14 22:42
 * @description: 健康资讯Controller
 */
@RestController
@RequestMapping("/article")
public class HealthArticleController {
    /**
     * 自动装配服务
     */
    @Resource
    private HealthArticleService healthArticleService;

    /**
     * 新增健康资讯
     *
     * @param healthArticleDTO 健康资讯信息
     * @return 新增操作响应结果
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public ApiResponse<String> save(@RequestBody HealthArticleDTO healthArticleDTO) {
        return healthArticleService.saveArticle(healthArticleDTO);
    }

    /**
     * 批量删除健康资讯
     *
     * @param ids 待删除的健康资讯ID集合
     * @return 删除操作响应结果
     */
    @PostMapping(value = "/deleteArticles")
    @ResponseBody
    public ApiResponse<String> deleteArticles(@RequestBody List<Integer> ids) {
        return healthArticleService.deleteArticles(ids);
    }

    /**
     * 修改健康资讯
     *
     * @param healthArticleDTO 修改后的健康资讯信息
     * @return 修改操作响应结果
     */
    @PostMapping(value = "/update")
    @ResponseBody
    public ApiResponse<String> update(@RequestBody HealthArticleDTO healthArticleDTO) {
        return healthArticleService.updateArticle(healthArticleDTO);
    }

    /**
     * 查询健康资讯（标题模糊查询、标签筛选、推荐标记筛选）
     *
     * @param healthArticleQueryDTO 查询参数（部分标题、标签ID、推荐标记）
     * @return 查询操作响应结果
     */
    @PostMapping(value = "/queryArticles")
    @ResponseBody
    public ApiResponse<List<HealthArticleVO>> queryHealthArticles(@RequestBody HealthArticleQueryDTO healthArticleQueryDTO) {
        return healthArticleService.queryArticles(healthArticleQueryDTO);
    }

    /**
     * 查询每个标签下的文章数
     *
     * @return 数据列表（标签信息（标签ID、标签名） + 标签下文章数） + 查询操作响应结果
     */
    @GetMapping(value = "/queryStatistics")
    @ResponseBody
    public ApiResponse<List<ArticleTagStatisticsVO>> queryHealthArticles() {
        return healthArticleService.queryArticleTagStatistics();
    }

    /**
     * 查询每天的文章数
     *
     * @return 每天的文章数 + 查询操作响应结果
     */
    @GetMapping(value = "/queryTrend")
    @ResponseBody
    public ApiResponse<List<ArticleTrendVO>> queryArticleTrend() {
        return healthArticleService.queryArticleTrend();
    }

    @PostMapping(value = "/queryDetail")
    @ResponseBody
    public ApiResponse<HealthArticleVO> queryArticleByArticleId(@RequestBody HealthArticleDTO healthArticleDTO) {
        return healthArticleService.queryArticleById(healthArticleDTO.getHealthArticleId(), healthArticleDTO.getUserId());
    }
}
