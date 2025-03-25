package com.example.health.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author huanghaiming
 * @date 2025/3/5 00:36
 * @description: 信息通知，对应数据库表notice
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDO {
    /**
     * 消息ID
     */
    private Integer noticeId;

    /**
     * 消息类型
     */
    private Integer noticeType;

    /**
     * 消息内容
     */
    private String noticeContent;

    /**
     * 接收者ID
     */
    private Integer receiverId;

    /**
     * 发送者ID
     */
    private Integer senderId;

    /**
     * 消息是否已读
     * 注意：其对应的数据库字段为is_read，但是在POJO类中任何布尔类型的变量都不要加is前缀
     */
    private Boolean read;

    /**
     * 关联信息ID（如哪个指标数据异常通知...）
     */
    private Integer contentId;

    /**
     * 信息发送时间
     */
    private LocalDateTime noticeCreateTime;
}
