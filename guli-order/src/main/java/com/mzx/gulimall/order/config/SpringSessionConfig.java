package com.mzx.gulimall.order.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.mzx.gulimall.common.constant.SpringSessionConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * 分布式Session序列化配置问题.
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-25 22:48 周五.
 */
@Configuration
public class SpringSessionConfig {

    @Bean
    public CookieSerializer cookieSerializer(){

        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        // 只要是访问该IP地址那么就共享.
        // 只要域名是localhost那么session就共享.
        serializer.setDomainName(SpringSessionConstant.COOKIE_DO_MAIN_NAME);
        serializer.setCookieName(SpringSessionConstant.COOKIE_NAME);
        serializer.setCookiePath(SpringSessionConstant.COOKIE_PATH);
        return serializer;
    }

    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {

        return new GenericFastJsonRedisSerializer();
    }

}
