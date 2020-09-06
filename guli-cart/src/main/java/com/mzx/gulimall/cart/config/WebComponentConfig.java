package com.mzx.gulimall.cart.config;

import com.mzx.gulimall.cart.interceptor.CartInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ZhenXinMa
 * @date 2020/9/6 9:48
 */
@Configuration
public class WebComponentConfig implements WebMvcConfigurer {

    @Autowired
    private CartInterceptor cartInterceptor;

    /**
     * 向当前IOC中注册一个拦截器.
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // addPathPatterns: 针对那些请求进行拦截。
        registry.addInterceptor(cartInterceptor).addPathPatterns("/**");
    }
}
