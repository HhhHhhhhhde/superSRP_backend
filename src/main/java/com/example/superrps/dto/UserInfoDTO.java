package com.example.superrps.dto;

import lombok.Data;

/**
 * 用户信息DTO
 */
@Data
public class UserInfoDTO {
    /**
     * 用户ID
     */
    private Long id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 头像URL
     */
    private String avatar;
    
    /**
     * 用户统计信息
     */
    private UserStatsDTO stats;
    
    /**
     * 用户统计DTO（内部类）
     */
    @Data
    public static class UserStatsDTO {
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
         * 胜率
         */
        public Double getWinRate() {
            if (gamesPlayed == null || gamesPlayed == 0) {
                return 0.0;
            }
            return (double) gamesWon / gamesPlayed * 100;
        }
    }
}

