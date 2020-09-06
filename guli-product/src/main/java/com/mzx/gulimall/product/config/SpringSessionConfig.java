package com.mzx.gulimall.product.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * @author ZhenXinMa
 * @date 2020/8/26 16:22
 */
@Configuration
public class SpringSessionConfig {


    @Bean
    public CookieSerializer cookieSerializer(){

        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        // 只要是访问该IP地址那么就共享.
        //  如果 127.0.0.1  不共享 那么就是用域名 + 子域名
        // 顶级域名: guli.com
        serializer.setDomainName("localhost");
        serializer.setCookieName("GULIMALL-SESSION");
        serializer.setCookiePath("/");
        return serializer;

    }

    /**
     * SpringSession 使用Redis存储的时候 存储为JSON值.
     * @return
     */
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {

        return new GenericFastJsonRedisSerializer();
    }


}
