package com.example.superrps.dao;

import com.example.superrps.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户数据访问接口
 */
@Mapper
public interface UserMapper {
    
    /**
     * 根据用户名查询用户
     */
    User findByUsername(@Param("username") String username);
    
    /**
     * 根据ID查询用户
     */
    User findById(@Param("id") Long id);
    
    /**
     * 插入用户
     */
    int insert(User user);
    
    /**
     * 更新最后登录时间
     */
    int updateLastLoginTime(@Param("id") Long id);
    
    /**
     * 更新用户信息
     */
    int update(User user);
}

