package com.example.health.api;

import com.example.health.em.HttpStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huanghaiming
 * @date 2025/3/10 10:44
 * @description: 返回数据给前端
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiResponse<T> {
    /**
     * 响应状态码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 数据分页
     */
    private Integer total;

    public ApiResponse(Integer code) {
        this.code = code;
        this.msg = "OK";
    }

    public ApiResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ApiResponse(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<T>(HttpStatusEnum.OK.getHttpStatusCode(), HttpStatusEnum.OK.getHttpStatusDescription(), null);
    }

    public static <T> ApiResponse<T> success(String msg) {
        return new ApiResponse<T>(HttpStatusEnum.OK.getHttpStatusCode(), msg, null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<T>(HttpStatusEnum.OK.getHttpStatusCode(), HttpStatusEnum.OK.getHttpStatusDescription(), data);
    }

    public static <T> ApiResponse<T> success(String msg,T data) {
        return new ApiResponse<T>(HttpStatusEnum.OK.getHttpStatusCode(), msg, data);
    }

    /**
     * 分页数据响应
     *
     * @param data 列表数据
     * @param total 总分页数
     * @return 创建响应
     * @param <T> VO类
     */
    public static <T> ApiResponse<T> success(T data, Integer total) {
        return new ApiResponse<T>(HttpStatusEnum.OK.getHttpStatusCode(), HttpStatusEnum.OK.getHttpStatusDescription(), data, total);
    }

    public static <T> ApiResponse<T> error(String msg) {
        return new ApiResponse<T>(HttpStatusEnum.BAD_REQUEST.getHttpStatusCode(), msg);
    }
}
