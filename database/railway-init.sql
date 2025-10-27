-- ================================================
-- Railway数据库初始化脚本（不需要CREATE DATABASE）
-- 直接在Railway的Query界面执行此文件
-- ================================================

-- 先删除旧表（如果存在）
DROP TABLE IF EXISTS `game_history`;
DROP TABLE IF EXISTS `user_stats`;
DROP TABLE IF EXISTS `users`;

-- ================================================
-- 1. 用户表
-- ================================================
CREATE TABLE `users` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(20) NOT NULL COMMENT '用户名（唯一）',
  `password` VARCHAR(255) NOT NULL COMMENT '密码（明文存储）',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_login_at` TIMESTAMP NULL DEFAULT NULL COMMENT '最后登录时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ================================================
-- 2. 用户统计表
-- ================================================
CREATE TABLE `user_stats` (
  `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
  `games_played` INT(11) NOT NULL DEFAULT 0 COMMENT '游戏总场次',
  `games_won` INT(11) NOT NULL DEFAULT 0 COMMENT '获胜场次',
  `current_level` INT(11) NOT NULL DEFAULT 1 COMMENT '当前关卡',
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`user_id`),
  CONSTRAINT `fk_stats_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户统计表';

-- ================================================
-- 3. 游戏历史记录表
-- ================================================
CREATE TABLE `game_history` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
  `game_type` VARCHAR(20) NOT NULL COMMENT '游戏类型（pvp-玩家对战/stage-闯关模式/ai-人机对战）',
  `level` INT(11) DEFAULT NULL COMMENT '关卡数（闯关模式使用）',
  `result` VARCHAR(10) NOT NULL COMMENT '游戏结果（win-获胜/lose-失败/draw-平局）',
  `moves` TEXT DEFAULT NULL COMMENT '游戏过程（JSON格式）',
  `played_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '游戏时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_played_at` (`played_at`),
  KEY `idx_game_type` (`game_type`),
  CONSTRAINT `fk_history_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='游戏历史记录表';

-- ================================================
-- 验证表创建成功
-- ================================================
SHOW TABLES;

