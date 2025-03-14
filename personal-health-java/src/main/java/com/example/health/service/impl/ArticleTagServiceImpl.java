package com.example.health.service.impl;

import com.example.health.api.ApiResponse;
import com.example.health.mapper.ArticleTagMapper;
import com.example.health.pojo.dto.ArticleTagDTO;
import com.example.health.pojo.entity.ArticleTagDO;
import com.example.health.pojo.vo.ArticleTagVO;
import com.example.health.service.ArticleTagService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author huanghaiming
 * @date 2025/3/14 10:16
 * @description: 健康资讯服务实现类
 */
@Service
public class ArticleTagServiceImpl implements ArticleTagService {
    /**
     * Mapper自动装配
     */
    @Resource
    private ArticleTagMapper articleTagMapper;

    /**
     * 新增标签
     *
     * @param articleTagDTO 新增标签信息
     * @return 新增操作响应结果
     */
    @Override
    public ApiResponse<String> saveArticleTag(ArticleTagDTO articleTagDTO) {
        ArticleTagDO articleTagDO = articleTagMapper.getByCondition(
                ArticleTagDO.builder().tagName(articleTagDTO.getTagName()).build()
        );
        return Optional.ofNullable(articleTagDO)
                .map(tag -> ApiResponse.<String>error("标签已存在")).orElseGet(() -> {
                    articleTagMapper.saveArticleTag(ArticleTagDO.builder().tagName(articleTagDTO.getTagName()).build());
                    return ApiResponse.success("标签添加成功");
                });
    }

    /**
     * 批量删除标签
     *
     * @param ids 待删除的标签ID集合
     * @return 删除操作响应结果
     */
    @Override
    public ApiResponse<String> deleteArticleTags(List<Integer> ids) {
        articleTagMapper.deleteArticleTags(ids);
        return ApiResponse.success("删除成功");
    }

    /**
     * 更新标签信息
     *
     * @param articleTagDTO 修改后的标签信息
     * @return 更新操作响应结果
     */
    @Override
    public ApiResponse<String> updateArticleTag(ArticleTagDTO articleTagDTO) {
        ArticleTagDO articleTagDO = articleTagMapper.getByCondition(
                ArticleTagDO.builder().tagId(articleTagDTO.getTagId()).build()
        );
        BeanUtils.copyProperties(articleTagDTO, articleTagDO);
        articleTagMapper.updateArticleTag(articleTagDO);
        return ApiResponse.success("修改成功");
    }

    /**
     * 分页+模糊查询标签
     *
     * @param articleTagDTO 标签模糊查询参数
     * @return 符合要求的标签信息列表
     */
    @Override
    public ApiResponse<List<ArticleTagVO>> queryArticleTags(ArticleTagDTO articleTagDTO) {
        ArticleTagDO articleTagDO = ArticleTagDO.builder()
                .tagName(articleTagDTO.getTagName()).build();
        List<ArticleTagDO> articleTagDOList = articleTagMapper.queryArticleTags(articleTagDO, articleTagDTO.getCurrent(), articleTagDTO.getSize());
        Integer count = articleTagMapper.queryCount(articleTagDO);
        List<ArticleTagVO> articleTagVOList = new ArrayList<>();
        for (ArticleTagDO tag : articleTagDOList) {
            ArticleTagVO articleTagVO = new ArticleTagVO();
            BeanUtils.copyProperties(tag, articleTagVO);
            articleTagVOList.add(articleTagVO);
        }
        return ApiResponse.success(articleTagVOList, count);
    }
}
