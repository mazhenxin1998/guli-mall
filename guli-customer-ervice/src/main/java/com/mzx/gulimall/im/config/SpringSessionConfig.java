package com.mzx.gulimall.im.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import javax.management.MXBean;

/**
 * @author ZhenXinMa
 * @date 2020/9/8 0:00
 */
@Configuration
public class SpringSessionConfig {

    @Bean
    public CookieSerializer cookieSerializer(){

        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        cookieSerializer.setDomainName("localhost");
        cookieSerializer.setCookieName("GULIMALL-SESSION");
        cookieSerializer.setCookiePath("/");
        return cookieSerializer;

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
