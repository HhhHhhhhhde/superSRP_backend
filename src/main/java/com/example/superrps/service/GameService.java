package com.example.superrps.service;

import com.example.superrps.dao.GameHistoryMapper;
import com.example.superrps.dao.UserStatsMapper;
import com.example.superrps.entity.GameHistory;
import com.example.superrps.entity.UserStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 游戏服务
 */
@Service
public class GameService {
    
    @Autowired
    private GameHistoryMapper gameHistoryMapper;
    
    @Autowired
    private UserStatsMapper userStatsMapper;
    
    private final Random random = new Random();
    
    /**
     * 保存游戏记录
     */
    @Transactional
    public void saveGameHistory(Long userId, String gameType, Integer level, 
                                 String result, String moves) {
        // 保存游戏记录
        GameHistory gameHistory = new GameHistory();
        gameHistory.setUserId(userId);
        gameHistory.setGameType(gameType);
        gameHistory.setLevel(level);
        gameHistory.setResult(result);
        gameHistory.setMoves(moves);
        gameHistory.setPlayedAt(new Date());
        
        gameHistoryMapper.insert(gameHistory);
        
        // 更新用户统计
        userStatsMapper.incrementGamesPlayed(userId);
        if ("win".equals(result)) {
            userStatsMapper.incrementGamesWon(userId);
            
            // 如果是闯关模式且获胜，更新关卡
            if ("stage".equals(gameType) && level != null) {
                UserStats stats = userStatsMapper.findByUserId(userId);
                if (stats != null && level >= stats.getCurrentLevel()) {
                    userStatsMapper.updateCurrentLevel(userId, level + 1);
                }
            }
        }
    }
    
    /**
     * 获取用户游戏历史
     */
    public List<GameHistory> getUserGameHistory(Long userId, Integer page, Integer pageSize) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        
        int offset = (page - 1) * pageSize;
        return gameHistoryMapper.findByUserId(userId, offset, pageSize);
    }
    
    /**
     * 统计用户游戏总数
     */
    public int countUserGames(Long userId) {
        return gameHistoryMapper.countByUserId(userId);
    }
    
    /**
     * 获取AI出招（简单AI逻辑）
     */
    public String getAIMove(int difficulty) {
        String[] moves = {"rock", "paper", "scissors"};
        return moves[random.nextInt(moves.length)];
    }
    
    /**
     * 判断游戏结果
     * @param playerMove 玩家出招
     * @param opponentMove 对手出招
     * @return win/lose/draw
     */
    public String judgeResult(String playerMove, String opponentMove) {
        if (playerMove.equals(opponentMove)) {
            return "draw";
        }
        
        if ((playerMove.equals("rock") && opponentMove.equals("scissors")) ||
            (playerMove.equals("scissors") && opponentMove.equals("paper")) ||
            (playerMove.equals("paper") && opponentMove.equals("rock"))) {
            return "win";
        }
        
        return "lose";
    }
}

