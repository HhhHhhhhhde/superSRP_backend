package com.example.superrps.dao;

import com.example.superrps.entity.UserStats;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户统计数据访问接口
 */
@Mapper
public interface UserStatsMapper {
    
    /**
     * 根据用户ID查询统计数据
     */
    UserStats findByUserId(@Param("userId") Long userId);
    
    /**
     * 插入统计数据
     */
    int insert(UserStats userStats);
    
    /**
     * 更新统计数据
     */
    int update(UserStats userStats);
    
    /**
     * 增加游戏场次
     */
    int incrementGamesPlayed(@Param("userId") Long userId);
    
    /**
     * 增加获胜场次
     */
    int incrementGamesWon(@Param("userId") Long userId);
    
    /**
     * 更新当前关卡
     */
    int updateCurrentLevel(@Param("userId") Long userId, @Param("level") Integer level);
}

