package com.mzx.gulimall.order.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.mzx.gulimall.common.constant.SpringSessionConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.RedisSerializer;


/**
 * @author ZhenXinMa.
 * @slogan 浮生若梦, 若梦非梦, 浮生何梦? 如梦之梦.
 * @create 2020-09-14 22:15 周一.
 */
@Configuration
public class SessionConfig {

    /**
     * 配置cookie的序列化器。
     * <p>
     * 也就是配置保存session的cookie的域的大小,为了能够让其子域进行访问.
     *
     * @return 返回默认的CookieSerializer序列化器.
     */
//    @Bean
//    public CookieSerializer cookieSerializer() {
//
//        // TODO: 2020/9/23 整个所有的配置都一样,那么这里为什么会出现错误呢?
//        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
//        serializer.setDomainName(SpringSessionConstant.COOKIE_DO_MAIN_NAME);
//        serializer.setCookiePath(SpringSessionConstant.COOKIE_PATH);
//        // 其实所有的字符串都不应该出现在业务逻辑中. 应该将其放在公共配置中.
//        serializer.setCookieName(SpringSessionConstant.COOKIE_NAME);
//        return serializer;
//
//    }

    /**
     * 对Redis进行序列化.
     * <p>
     * 使其存入的数据为JSON形式.
     *
     * @return 返回fastJson序列化器.
     */
    @Bean
    public RedisSerializer<Object> redisSerializer() {

        return new GenericFastJsonRedisSerializer();

    }


}
