package com.mzx.gulimall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * @author ZhenXinMa
 * @date 2020/7/12 13:02
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {

        CorsConfiguration config = new CorsConfiguration();
        // 云讯那个来源访问.
        config.addAllowedOrigin("*");
        // 允许的头部信息.
        config.addAllowedHeader("*");
        // 允许所有请求方式.
        config.addAllowedMethod("*");
        // 配置是否允许携带Cookie信息.
        config.setAllowCredentials(true);
        // 我们拦截所有请求.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }

}
