package com.example.health.pojo.dto.query.extend;

import com.example.health.pojo.dto.query.base.QueryDTO;
import lombok.*;

/**
 * @author huanghaiming
 * @date 2025/3/5 14:47
 * @description: 用户收藏健康资讯记录查询DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSavedArticleQueryDTO extends QueryDTO {
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
