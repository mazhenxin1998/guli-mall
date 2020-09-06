package com.mzx.gulimall.cart.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * @author ZhenXinMa
 * @date 2020/9/4 7:38
 */
@Configuration
public class SessionConfig {

    /**
     * Cookie序列化器, 可以让指定域名以及其子域名共同访问其cookie.
     *
     * @return
     */
    @Bean
    public CookieSerializer cookieSerializer() {

        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        // 只要是访问该IP地址那么就共享.
        // 只要域名是localhost那么session就共享.
        serializer.setDomainName("localhost");
        serializer.setCookieName("GULIMALL-SESSION");
        serializer.setCookiePath("/");
        return serializer;
    }


    /**
     * SpringSession序列化器.
     *
     * @return
     */
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {

        return new GenericFastJsonRedisSerializer();
    }

}
