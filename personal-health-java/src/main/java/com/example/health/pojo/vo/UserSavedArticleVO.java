package com.example.health.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author huanghaiming
 * @date 2025/3/16 00:20
 * @description: 用户收藏资讯视图对象（与DO相比，多出了资讯的相关信息）
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSavedArticleVO {
    /**
     * 资讯收藏记录编号
     */
    private Integer savedId;

    /**
     * 健康资讯ID
     */
    private Integer articleId;

    /**
     * 用户收藏健康资讯的时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime savedCreateTime;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 健康资讯封面
     */
    private String healthArticleCover;

    /**
     * 健康资讯标题
     */
    private String healthArticleTitle;

    /**
     * 健康资讯内容
     */
    private String healthArticleContent;

    /**
     * 健康资讯标签名
     */
    private String healthArticleTagName;

    /**
     * 健康资讯发布时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime healthArticleCreateTime;
}
