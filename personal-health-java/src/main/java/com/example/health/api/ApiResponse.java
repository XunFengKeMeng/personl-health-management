package com.example.health.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
        return new ApiResponse<T>(200, "OK", null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<T>(200, "OK", data);
    }

    public static <T> ApiResponse<T> error(String msg) {
        return new ApiResponse<T>(404, msg);
    }
}
