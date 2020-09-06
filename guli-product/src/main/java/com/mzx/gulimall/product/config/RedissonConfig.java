package com.mzx.gulimall.product.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author ZhenXinMa
 * @date 2020/8/6 21:00
 */
@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient() {

        // 默认连接地址 127.0.0.1:6379
        Config config = new Config();
        // redis://127.0.0.1:6379
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }


}
