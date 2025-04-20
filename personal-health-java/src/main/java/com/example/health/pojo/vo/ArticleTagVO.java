package com.example.health.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author huanghaiming
 * @date 2025/3/13 22:57
 * @description: 健康资讯标签视图对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleTagVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 标签ID
     */
    private Integer tagId;

    /**
     * 标签名
     */
    private String tagName;
}
