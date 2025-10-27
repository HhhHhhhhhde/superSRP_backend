package com.example.superrps.entity;

import lombok.Data;
import java.util.Date;

/**
 * 用户统计实体类
 */
@Data
public class UserStats {
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 游戏总场次
     */
    private Integer gamesPlayed;
    
    /**
     * 获胜场次
     */
    private Integer gamesWon;
    
    /**
     * 当前关卡
     */
    private Integer currentLevel;
    
    /**
     * 更新时间
     */
    private Date updatedAt;
}

