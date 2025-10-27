package com.example.superrps.config;

import com.example.superrps.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

/**
 * Web配置类
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Autowired
    private AuthInterceptor authInterceptor;
    
    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 需要认证的路径
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/game/save", "/api/game/history", "/api/auth/me")
                .excludePathPatterns(
                        "/api/auth/login",
                        "/api/auth/register",
                        "/api/auth/check",
                        "/api/auth/logout",
                        "/api/game/ai-move",
                        "/api/game/judge"
                );
    }
    
    /**
     * CORS配置（跨域）- 使用CorsFilter确保优先处理
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // 允许特定域名跨域（更安全的配置）
        config.addAllowedOrigin("http://localhost:8081");  // 本地开发
        config.addAllowedOrigin("https://supersrp-production.up.railway.app");  // 生产环境前端
        
        // 允许所有请求头
        config.addAllowedHeader("*");
        
        // 允许所有请求方法
        config.addAllowedMethod("*");
        
        // 允许携带认证信息（如Cookie、Authorization header）
        config.setAllowCredentials(true);
        
        // 暴露哪些头部信息（可选）
        config.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
        
        // 预检请求的缓存时间（秒）
        config.setMaxAge(3600L);
        
        // 对所有路径应用这个配置
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
    
    /**
     * 额外的CORS配置（双重保险）
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}

