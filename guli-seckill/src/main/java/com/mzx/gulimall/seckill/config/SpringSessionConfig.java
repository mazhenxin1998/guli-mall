package com.mzx.gulimall.seckill.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.mzx.gulimall.common.constant.SpringSessionConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * SpringSession配置文件.
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-26 14:43 周一.
 */
@Configuration
public class SpringSessionConfig {

    /**
     * 配置Cookie子域共享Cookie.
     *
     * @return
     */
    @Bean
    public CookieSerializer cookieSerializer() {

        DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
        defaultCookieSerializer.setCookieName(SpringSessionConstant.COOKIE_NAME);
        defaultCookieSerializer.setDomainName(SpringSessionConstant.COOKIE_DO_MAIN_NAME);
        defaultCookieSerializer.setCookiePath(SpringSessionConstant.COOKIE_PATH);
        return defaultCookieSerializer;

    }

    /**
     * 配置SpringSession存储SpringSession信息的时候以JSON格式进行存储.
     *
     * @return
     */
    @Bean
    public RedisSerializer<Object> redisSerializer() {

        return new GenericFastJsonRedisSerializer();

    }


}
