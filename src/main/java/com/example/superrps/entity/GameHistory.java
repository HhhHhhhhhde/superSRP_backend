package com.example.superrps.entity;

import lombok.Data;
import java.util.Date;

/**
 * 游戏历史记录实体类
 */
@Data
public class GameHistory {
    /**
     * 记录ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 游戏类型（pvp-玩家对战/stage-闯关模式/ai-人机对战）
     */
    private String gameType;
    
    /**
     * 关卡数（闯关模式使用）
     */
    private Integer level;
    
    /**
     * 游戏结果（win-获胜/lose-失败/draw-平局）
     */
    private String result;
    
    /**
     * 游戏过程（JSON格式）
     */
    private String moves;
    
    /**
     * 游戏时间
     */
    private Date playedAt;
}

