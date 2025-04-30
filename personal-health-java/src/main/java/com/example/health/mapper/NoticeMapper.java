package com.example.health.mapper;

import com.example.health.pojo.entity.NoticeDO;
import com.example.health.pojo.vo.NoticeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/3/5 09:51
 * @description: 通知持久层接口
 */
@Mapper
public interface NoticeMapper {

    /**
     * 批量增加通知
     *
     * @param notices 新增的若干通知信息
     */
    void insertNotices(
            @Param("notice")NoticeDO notice,
            @Param("ids")List<Integer> receiverIds);

    /**
     * 新增单条通知（系统发送
     * ）
     * @param notice 通知信息
     */
    void insertNotice(NoticeDO notice);

    /**
     * 发布全体通知
     *
     * @param notices 新增的若干通知信息
     */
    void insertAll(NoticeDO notice);

    /**
     * 批量删除通知
     *
     * @param ids 待删除的通知ID集合
     */
    void deleteNotices(@Param(value = "ids") List<Integer> ids);

    /**
     * 更新通知信息（主要用于更新通知是否已读）
     *
     * @param notice 修改后的通知信息
     */
    void updateNotice(NoticeDO notice);

    /**
     * 分页查询通知
     *
     * @param notice 查询参数（通知类型、接收者/发送者ID、是否已读）
     * @return 满足查询条件的通知信息列表
     */
    List<NoticeVO> queryNotices(
            @Param("notice") NoticeDO notice,
            @Param("current") Integer current,
            @Param("size") Integer size);


    /**
     * 查询到的通知数
     *
     * @param notice 查询参数
     * @return 满足查询条件的通知数
     */
    Integer queryCount(NoticeDO notice);
}
