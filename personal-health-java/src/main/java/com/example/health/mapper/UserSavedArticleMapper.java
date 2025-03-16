package com.example.health.mapper;

import com.example.health.pojo.entity.UserSavedArticleDO;
import com.example.health.pojo.vo.UserSavedArticleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/3/5 09:50
 * @description: 用户收藏资讯持久层接口
 */
@Mapper
public interface UserSavedArticleMapper {
    /**
     * 新增收藏记录
     *
     * @param userSavedArticleDO 收藏信息
     */
    void save(UserSavedArticleDO userSavedArticleDO);

    /**
     * 批量删除收藏记录
     *
     * @param ids 待删除的收藏记录ID集合
     */
    void deleteSavedArticles(@Param("ids") List<Integer> ids);

    /**
     * 查询收藏记录（主要用于检查数据库内有无记录）
     *
     * @param userSavedArticleDO 收藏记录信息
     * @return 空/查询到的收藏记录
     */
    UserSavedArticleDO querySavedArticle(UserSavedArticleDO userSavedArticleDO);

    /**
     * 分页查询收藏记录
     *
     * @param userSavedArticleDO 查询参数
     * @param current 当前页
     * @param size 页大小
     * @return 满足查询参数的收藏记录列表
     */
    List<UserSavedArticleVO> querySavedArticles(
            @Param("saved") UserSavedArticleDO userSavedArticleDO,
            @Param("current") Integer current,
            @Param("size") Integer size);

    /**
     * 满足查询参数的数据数
     *
     * @param userSavedArticleDO 查询参数
     * @return 满足查询参数的收藏记录数
     */
    Integer queryCount(UserSavedArticleDO userSavedArticleDO);
}
