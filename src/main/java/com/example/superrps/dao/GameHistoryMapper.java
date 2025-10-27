package com.example.superrps.dao;

import com.example.superrps.entity.GameHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 游戏历史记录数据访问接口
 */
@Mapper
public interface GameHistoryMapper {
    
    /**
     * 插入游戏记录
     */
    int insert(GameHistory gameHistory);
    
    /**
     * 根据用户ID查询游戏记录（支持分页）
     */
    List<GameHistory> findByUserId(@Param("userId") Long userId, 
                                    @Param("offset") Integer offset, 
                                    @Param("limit") Integer limit);
    
    /**
     * 统计用户游戏总数
     */
    int countByUserId(@Param("userId") Long userId);
}

