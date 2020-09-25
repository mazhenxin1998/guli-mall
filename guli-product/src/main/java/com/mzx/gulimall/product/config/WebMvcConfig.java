package com.mzx.gulimall.product.config;

import com.mzx.gulimall.annotation.interceptor.PreventBrushInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-25 20:53 周五.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 从自定义的annotation包中引入自定义的接口防刷拦截器.
     * 需要配合annotation包中的注解@AccessLimit一起使用.
     */
    @Autowired
    private PreventBrushInterceptor preventBrushInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(preventBrushInterceptor).addPathPatterns("/**");

    }
}
