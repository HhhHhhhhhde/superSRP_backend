package com.example.superrps.dto;

import lombok.Data;

/**
 * 认证检查响应对象
 */
@Data
public class AuthCheckResponse {
    /**
     * 是否已认证
     */
    private boolean authenticated;
    
    /**
     * 用户信息（如果已认证）
     */
    private UserInfoDTO userInfo;
    
    public AuthCheckResponse(boolean authenticated, UserInfoDTO userInfo) {
        this.authenticated = authenticated;
        this.userInfo = userInfo;
    }
}

