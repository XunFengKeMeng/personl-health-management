package com.example.health.controller;

import com.example.health.api.ApiResponse;
import com.example.health.pojo.dto.NoticeDTO;
import com.example.health.pojo.dto.NoticeListDTO;
import com.example.health.pojo.vo.NoticeVO;
import com.example.health.service.NoticeService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author huanghaiming
 * @date 2025/3/17 14:43
 * @description: 通知Controller
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {
    /**
     * 注入服务
     */
    @Resource
    private NoticeService noticeService;

    /**
     * 新增若干通知
     *
     * @param noticeDTOList 新增的通知信息列表
     * @return 新增操作响应结果
     */
    @RequestMapping(value = "/insertNotices")
    @ResponseBody
    public ApiResponse<String> insertNotices(@RequestBody NoticeListDTO noticeListDTO) {
        return noticeService.insertNotices(noticeListDTO);
    }

    /**
     * 批量删除通知
     *
     * @param ids 待删除的通知ID集合
     * @return 删除操作响应结果
     */
    @RequestMapping(value = "/deleteNotices")
    @ResponseBody
    public ApiResponse<String> deleteNotices(@RequestBody List<Integer> ids) {
        return noticeService.deleteNotices(ids);
    }

    /**
     * 更新单条通知信息
     *
     * @param noticeDTO 修改后的通知信息（包括通知ID及通知已读标记即可）
     * @return 更新操作响应结果
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public ApiResponse<String> update(@RequestBody NoticeDTO noticeDTO) {
        return noticeService.updateNotice(noticeDTO);
    }

    /**
     * 分页查询通知信息
     *
     * @param noticeDTO 查询参数 + 页参数
     * @return 满足条件的通知视图对象列表 + 查询操作响应结果
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public ApiResponse<List<NoticeVO>> queryNotices(@RequestBody NoticeDTO noticeDTO) {
        return noticeService.queryNotices(noticeDTO);
    }
}
