package com.example.health.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author huanghaiming
 * @date 2025/3/17 09:53
 * @description: 通知视图对象（与DO相比，将接收者/发送者ID修改为名字）
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeVO {
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
     * 接收者名字
     */
    private String receiverName;

    /**
     * 发送者名字
     */
    private String senderName;

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
