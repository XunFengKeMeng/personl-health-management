package com.example.health.pojo.dto;

import com.example.health.pojo.dto.query.base.QueryDTO;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author huanghaiming
 * @date 2025/3/15 22:17
 * @description: 用户收藏健康资讯的数据传输对象
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSavedArticleDTO extends QueryDTO {
    /**
     * 资讯收藏记录编号
     */
    private Integer savedId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 健康资讯ID
     */
    private Integer articleId;
}
