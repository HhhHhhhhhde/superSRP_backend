package com.example.superrps.controller;

import com.example.superrps.dto.ApiResponse;
import com.example.superrps.entity.GameHistory;
import com.example.superrps.service.GameService;
import com.example.superrps.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 游戏控制器
 */
@RestController
@RequestMapping("/api/game")
public class GameController {
    
    @Autowired
    private GameService gameService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 获取AI出招
     */
    @PostMapping("/ai-move")
    public ApiResponse<Map<String, String>> getAIMove(@RequestBody Map<String, Object> request) {
        Integer difficulty = (Integer) request.getOrDefault("difficulty", 1);
        String aiMove = gameService.getAIMove(difficulty);
        
        Map<String, String> response = new HashMap<>();
        response.put("move", aiMove);
        return ApiResponse.success(response);
    }
    
    /**
     * 判断游戏结果
     */
    @PostMapping("/judge")
    public ApiResponse<Map<String, String>> judgeGame(@RequestBody Map<String, String> request) {
        String playerMove = request.get("playerMove");
        String opponentMove = request.get("opponentMove");
        
        String result = gameService.judgeResult(playerMove, opponentMove);
        
        Map<String, String> response = new HashMap<>();
        response.put("result", result);
        return ApiResponse.success(response);
    }
    
    /**
     * 保存游戏记录
     */
    @PostMapping("/save")
    public ApiResponse<Void> saveGame(@RequestBody Map<String, Object> request, 
                                       HttpServletRequest httpRequest) {
        String token = extractToken(httpRequest);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ApiResponse.error("未登录");
        }
        
        try {
            Long userId = jwtUtil.getUserIdFromToken(token);
            String gameType = (String) request.get("gameType");
            Integer level = request.get("level") != null ? 
                            Integer.valueOf(request.get("level").toString()) : null;
            String result = (String) request.get("result");
            String moves = request.get("moves") != null ? 
                          request.get("moves").toString() : null;
            
            gameService.saveGameHistory(userId, gameType, level, result, moves);
            return ApiResponse.success();
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 获取游戏历史
     */
    @GetMapping("/history")
    public ApiResponse<Map<String, Object>> getGameHistory(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest httpRequest) {
        
        String token = extractToken(httpRequest);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ApiResponse.error("未登录");
        }
        
        try {
            Long userId = jwtUtil.getUserIdFromToken(token);
            List<GameHistory> history = gameService.getUserGameHistory(userId, page, pageSize);
            int total = gameService.countUserGames(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("list", history);
            response.put("total", total);
            response.put("page", page);
            response.put("pageSize", pageSize);
            
            return ApiResponse.success(response);
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

