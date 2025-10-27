package com.example.superrps.dto;

import lombok.Data;

/**
 * 统一API响应对象（兼容前端格式）
 */
@Data
public class ApiResponse<T> {
    /**
     * 是否成功（前端使用）
     */
    private boolean success;
    
    /**
     * 消息
     */
    private String message;
    
    /**
     * 数据
     */
    private T data;
    
    /**
     * 状态码（可选，兼容旧代码）
     */
    private Integer code;
    
    /**
     * 成功响应
     */
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setCode(200);
        response.setMessage("success");
        response.setData(data);
        return response;
    }
    
    /**
     * 成功响应（无数据）
     */
    public static <T> ApiResponse<T> success() {
        return success(null);
    }
    
    /**
     * 成功响应（自定义消息）
     */
    public static <T> ApiResponse<T> success(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setCode(200);
        response.setMessage(message);
        return response;
    }
    
    /**
     * 失败响应
     */
    public static <T> ApiResponse<T> error(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(500);
        response.setMessage(message);
        return response;
    }
    
    /**
     * 自定义响应
     */
    public static <T> ApiResponse<T> custom(boolean success, int code, String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(success);
        response.setCode(code);
        response.setMessage(message);
        response.setData(data);
        return response;
    }
}

