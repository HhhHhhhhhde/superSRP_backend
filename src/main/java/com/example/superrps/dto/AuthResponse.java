package com.example.superrps.dto;

import lombok.Data;

/**
 * 认证响应对象（前端格式：data包含user和token）
 */
@Data
public class AuthResponse {
    /**
     * JWT token
     */
    private String token;
    
    /**
     * 用户信息（前端期望的字段名是user）
     */
    private UserInfoDTO user;
    
    public AuthResponse(String token, UserInfoDTO user) {
        this.token = token;
        this.user = user;
    }
}

