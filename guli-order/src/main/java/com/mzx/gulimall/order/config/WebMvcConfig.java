package com.mzx.gulimall.order.config;

import com.mzx.gulimall.order.interceptor.PreventBrushInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-25 16:36 周五.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private PreventBrushInterceptor preventBrushInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // **拦截所有请求.
        registry.addInterceptor(preventBrushInterceptor).addPathPatterns("/**");

    }
}
