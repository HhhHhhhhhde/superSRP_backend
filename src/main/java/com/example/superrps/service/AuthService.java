package com.example.superrps.service;

import com.example.superrps.dao.UserMapper;
import com.example.superrps.dao.UserStatsMapper;
import com.example.superrps.dto.AuthResponse;
import com.example.superrps.dto.LoginRequest;
import com.example.superrps.dto.RegisterRequest;
import com.example.superrps.dto.UserInfoDTO;
import com.example.superrps.entity.User;
import com.example.superrps.entity.UserStats;
import com.example.superrps.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 认证服务
 */
@Service
public class AuthService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private UserStatsMapper userStatsMapper;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 用户注册
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // 检查用户名是否已存在
        User existingUser = userMapper.findByUsername(request.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 创建新用户（密码明文存储）
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());  // 明文密码
        user.setEmail(request.getEmail());
        user.setCreatedAt(new Date());
        user.setLastLoginAt(new Date());
        
        userMapper.insert(user);
        
        // 初始化用户统计数据
        UserStats userStats = new UserStats();
        userStats.setUserId(user.getId());
        userStats.setGamesPlayed(0);
        userStats.setGamesWon(0);
        userStats.setCurrentLevel(1);
        userStatsMapper.insert(userStats);
        
        // 生成Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        
        // 返回认证响应
        UserInfoDTO userInfo = buildUserInfoDTO(user, userStats);
        return new AuthResponse(token, userInfo);
    }
    
    /**
     * 用户登录
     */
    public AuthResponse login(LoginRequest request) {
        // 查找用户
        User user = userMapper.findByUsername(request.getUsername());
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        // 验证密码（明文比对）
        if (!request.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        // 更新最后登录时间
        userMapper.updateLastLoginTime(user.getId());
        
        // 获取用户统计数据
        UserStats userStats = userStatsMapper.findByUserId(user.getId());
        if (userStats == null) {
            // 如果统计数据不存在，创建默认数据
            userStats = new UserStats();
            userStats.setUserId(user.getId());
            userStats.setGamesPlayed(0);
            userStats.setGamesWon(0);
            userStats.setCurrentLevel(1);
            userStatsMapper.insert(userStats);
        }
        
        // 生成Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        
        // 返回认证响应
        UserInfoDTO userInfo = buildUserInfoDTO(user, userStats);
        return new AuthResponse(token, userInfo);
    }
    
    /**
     * 获取用户信息
     */
    public UserInfoDTO getUserInfo(Long userId) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        UserStats userStats = userStatsMapper.findByUserId(userId);
        if (userStats == null) {
            userStats = new UserStats();
            userStats.setUserId(userId);
            userStats.setGamesPlayed(0);
            userStats.setGamesWon(0);
            userStats.setCurrentLevel(1);
        }
        
        return buildUserInfoDTO(user, userStats);
    }
    
    /**
     * 构建用户信息DTO
     */
    private UserInfoDTO buildUserInfoDTO(User user, UserStats userStats) {
        UserInfoDTO userInfo = new UserInfoDTO();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setEmail(user.getEmail());
        userInfo.setAvatar(user.getAvatar());
        
        UserInfoDTO.UserStatsDTO stats = new UserInfoDTO.UserStatsDTO();
        stats.setGamesPlayed(userStats.getGamesPlayed());
        stats.setGamesWon(userStats.getGamesWon());
        stats.setCurrentLevel(userStats.getCurrentLevel());
        
        userInfo.setStats(stats);
        return userInfo;
    }
}

