package com.mzx.gulimall.order.config;

import com.mzx.gulimall.common.constant.RedissonConstant;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ZhenXinMa.
 * @slogan 浮生若梦, 若梦非梦, 浮生何梦? 如梦之梦.
 * @create 2020-09-14 22:57 周一.
 */
@Configuration
public class RedissonConfig {

//    @Bean
//    public RedissonClient redissonClient(){
//
//        Config config = new Config();
//        config.useSingleServer().setAddress(RedissonConstant.REDISSON_ADDRESS);
//        RedissonClient redissonClient = Redisson.create(config);
//        return redissonClient;
//
//    }

}
