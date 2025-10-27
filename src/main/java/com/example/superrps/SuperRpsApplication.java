package com.example.superrps;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 超级石头剪刀布游戏后端主应用程序
 */
@SpringBootApplication
@MapperScan("com.example.superrps.dao")
public class SuperRpsApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SuperRpsApplication.class, args);
        System.out.println("========================================");
        System.out.println("超级石头剪刀布后端服务已启动！");
        System.out.println("访问地址: http://localhost:8080");
        System.out.println("========================================");
    }
}

