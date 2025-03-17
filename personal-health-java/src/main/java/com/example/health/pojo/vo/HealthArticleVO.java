package com.example.health.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author huanghaiming
 * @date 2025/3/14 19:23
 * @description: 健康资讯视图对象（与DO一样）
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HealthArticleVO {
    /**
     * 健康资讯ID
     */
    private Integer healthArticleId;

    /**
     * 健康资讯标题
     */
    private String healthArticleTitle;

    /**
     * 健康资讯内容
     */
    private String healthArticleContent;

    /**
     * 健康资讯标签ID
     */
    private Integer healthArticleTagId;

    /**
     * 健康资讯封面
     */
    private String healthArticleCover;

    /**
     * 健康资讯阅读者ID列表，用“,”分隔
     */
    private String healthArticleReaderIds;

    /**
     * 是否推荐
     * 注意：其对应的数据库字段为is_top，但是在POJO类中任何布尔类型的变量都不要加is前缀
     */
    private Boolean Top;

    /**
     * 健康资讯发布时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime healthArticleCreateTime;
}
