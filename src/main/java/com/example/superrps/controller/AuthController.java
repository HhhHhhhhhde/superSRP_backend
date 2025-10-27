package com.example.superrps.controller;

import com.example.superrps.dto.*;
import com.example.superrps.service.AuthService;
import com.example.superrps.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 检查认证状态
     */
    @GetMapping("/check")
    public ApiResponse<AuthCheckResponse> checkAuth(HttpServletRequest request) {
        String token = extractToken(request);
        
        if (token == null || !jwtUtil.validateToken(token)) {
            AuthCheckResponse response = new AuthCheckResponse(false, null);
            return ApiResponse.success(response);
        }
        
        try {
            Long userId = jwtUtil.getUserIdFromToken(token);
            UserInfoDTO userInfo = authService.getUserInfo(userId);
            AuthCheckResponse response = new AuthCheckResponse(true, userInfo);
            return ApiResponse.success(response);
        } catch (Exception e) {
            AuthCheckResponse response = new AuthCheckResponse(false, null);
            return ApiResponse.success(response);
        }
    }
    
    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        // 前端使用JWT，登出只需清除前端token即可
        // 后端可以在此记录登出日志或进行其他处理
        return ApiResponse.success("退出登录成功");
    }
    
    /**
     * 获取当前用户信息
     */
    @GetMapping("/me")
    public ApiResponse<UserInfoDTO> getCurrentUser(HttpServletRequest request) {
        String token = extractToken(request);
        
        if (token == null || !jwtUtil.validateToken(token)) {
            return ApiResponse.error("未登录");
        }
        
        try {
            Long userId = jwtUtil.getUserIdFromToken(token);
            UserInfoDTO userInfo = authService.getUserInfo(userId);
            return ApiResponse.success(userInfo);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 从请求中提取Token
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

