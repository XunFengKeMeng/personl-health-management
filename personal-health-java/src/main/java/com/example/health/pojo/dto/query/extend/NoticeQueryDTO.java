package com.example.health.pojo.dto.query.extend;

import com.example.health.pojo.dto.query.base.QueryDTO;
import lombok.*;

/**
 * @author huanghaiming
 * @date 2025/3/5 14:54
 * @description: 通知查询DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeQueryDTO extends QueryDTO {
    /**
     * 消息ID
     */
    private Integer noticeId;

    /**
     * 接收者ID
     */
    private Integer receiverId;

    /**
     * 发送者ID
     */
    private Integer senderId;

    /**
     * 消息类型
     */
    private String noticeType;

    /**
     * 消息内容
     */
    private String noticeContent;

    /**
     * 消息是否已读
     */
    private Boolean isRead;

    /**
     * 关联信息ID（如哪个指标数据异常通知...）
     */
    private Integer contentId;

}
