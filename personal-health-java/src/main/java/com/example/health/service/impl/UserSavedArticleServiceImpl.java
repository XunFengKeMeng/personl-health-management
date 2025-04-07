package com.example.health.service.impl;

import com.example.health.api.ApiResponse;
import com.example.health.mapper.UserSavedArticleMapper;
import com.example.health.pojo.dto.UserSavedArticleDTO;
import com.example.health.pojo.entity.UserSavedArticleDO;
import com.example.health.pojo.vo.UserSavedArticleVO;
import com.example.health.service.UserSavedArticleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author huanghaiming
 * @date 2025/3/16 01:09
 * @description: 用户收藏健康资讯服务实现类
 */
@Service
public class UserSavedArticleServiceImpl implements UserSavedArticleService {
    /**
     * 注入持久层接口
     */
    @Resource
    UserSavedArticleMapper userSavedArticleMapper;

    /**
     * 新增收藏记录
     *
     * @param userSavedArticleDTO 收藏信息（用户ID，健康资讯ID）
     * @return 新增操作响应结果
     */
    @Override
    public ApiResponse<String> save(UserSavedArticleDTO userSavedArticleDTO) {
        UserSavedArticleDO userSavedArticleDO = UserSavedArticleDO.builder()
                .userId(userSavedArticleDTO.getUserId())
                .articleId(userSavedArticleDTO.getArticleId())
                .savedCreateTime(LocalDateTime.now()).build();
        return Optional.ofNullable(userSavedArticleMapper.querySavedArticle(userSavedArticleDO))
                .map(saved -> ApiResponse.<String>error("记录已存在"))
                .orElseGet(() -> {
                    userSavedArticleMapper.save(userSavedArticleDO);
                    return ApiResponse.success("记录新增成功");
                });
    }

    /**
     * 批量删除收藏记录
     *
     * @param ids 待删除的收藏记录ID集合
     * @return 删除操作响应结果
     */
    @Override
    public ApiResponse<String> deleteSavedArticles(List<Integer> ids) {
        userSavedArticleMapper.deleteSavedArticles(ids);
        return ApiResponse.success("记录删除成功");
    }

    /**
     * 根据用户ID和资讯ID删除指定收藏记录
     *
     * @param userSavedArticleDTO 待删除的收藏记录信息
     * @return 删除操作响应结果
     */
    @Override
    public ApiResponse<String> deleteOneSaved(UserSavedArticleDTO userSavedArticleDTO) {
        UserSavedArticleDO userSavedArticleDO = UserSavedArticleDO.builder()
                .userId(userSavedArticleDTO.getUserId())
                .articleId(userSavedArticleDTO.getArticleId())
                .build();
        userSavedArticleMapper.deleteOneSaved(userSavedArticleDO);
        return ApiResponse.success("记录删除成功");
    }

    /**
     * 分页查询收藏记录
     *
     * @param userSavedArticleDTO 查询参数（用户ID/健康资讯ID）
     * @return 查询操作响应结果 + 满足查询参数的记录列表
     */
    @Override
    public ApiResponse<List<UserSavedArticleVO>> querySavedArticles(UserSavedArticleDTO userSavedArticleDTO) {
        UserSavedArticleDO userSavedArticleDO = UserSavedArticleDO.builder()
                .userId(userSavedArticleDTO.getUserId())
                .articleId(userSavedArticleDTO.getArticleId()).build();
        List<UserSavedArticleVO> userSavedArticleVOList = userSavedArticleMapper.querySavedArticles(
                userSavedArticleDO,
                userSavedArticleDTO.getCurrent(),
                userSavedArticleDTO.getSize());
        Integer count = userSavedArticleMapper.queryCount(userSavedArticleDO);
        return ApiResponse.success(userSavedArticleVOList, count);
    }
}
