package com.mzx.gulimall.annotation.config;

import com.mzx.gulimall.annotation.constant.RedisConstant;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-25 21:03 周五.
 */
//@Configuration
//public class RedissonConfig {
//
//    @Bean
//    public RedissonClient redissonClient(){
//
//        Config config = new Config();
//        config.useSingleServer().setAddress(RedisConstant.REDISSON_ADDRESS);
//        RedissonClient redissonClient = Redisson.create(config);
//        return redissonClient;
//
//    }
//
//}
