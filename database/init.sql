-- ================================================
-- 超级石头剪刀布 - 数据库初始化脚本
-- ================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `super_rps` 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE `super_rps`;

-- ================================================
-- 1. 用户表
-- ================================================
DROP TABLE IF EXISTS `users`;

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
DROP TABLE IF EXISTS `user_stats`;

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
DROP TABLE IF EXISTS `game_history`;

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
-- 插入测试数据（可选）
-- ================================================

-- 测试用户（密码为明文：123456）
-- 注意：本项目使用明文密码存储
INSERT INTO `users` (`username`, `password`, `email`, `created_at`, `last_login_at`) VALUES
('testuser', '123456', 'test@example.com', NOW(), NOW()),
('player1', '123456', 'player1@example.com', NOW(), NOW()),
('admin', '123456', 'admin@example.com', NOW(), NOW());

-- 测试用户统计数据
INSERT INTO `user_stats` (`user_id`, `games_played`, `games_won`, `current_level`) VALUES
(1, 10, 7, 3),
(2, 5, 2, 1),
(3, 20, 15, 5);

-- 测试游戏记录
INSERT INTO `game_history` (`user_id`, `game_type`, `level`, `result`, `moves`, `played_at`) VALUES
(1, 'stage', 1, 'win', '{"rounds":[{"player":"rock","opponent":"scissors"}]}', NOW()),
(1, 'pvp', NULL, 'win', '{"rounds":[{"player":"paper","opponent":"rock"}]}', NOW()),
(2, 'stage', 1, 'lose', '{"rounds":[{"player":"scissors","opponent":"rock"}]}', NOW());

-- ================================================
-- 查询验证
-- ================================================

-- 查看所有用户
SELECT * FROM users;

-- 查看用户统计
SELECT 
    u.id,
    u.username,
    us.games_played,
    us.games_won,
    us.current_level
FROM users u
LEFT JOIN user_stats us ON u.id = us.user_id;

-- 查看游戏历史
SELECT 
    gh.id,
    u.username,
    gh.game_type,
    gh.level,
    gh.result,
    gh.played_at
FROM game_history gh
JOIN users u ON gh.user_id = u.id
ORDER BY gh.played_at DESC;

-- ================================================
-- 说明
-- ================================================
-- 1. 测试用户的密码都是：123456（明文存储）
-- 2. 本项目使用明文密码存储
-- 3. 如果不需要测试数据，请删除"插入测试数据"部分
-- 4. 请根据实际情况修改数据库连接配置（application.yml）
-- ================================================

