package com.mzx.gulimall.seckill.config;

import com.mzx.gulimall.common.constant.RedissonConstant;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-26 15:07 周一.
 */
@Configuration
public class RedissonConfig {

    /**
     * 向容器中注入操作分布式锁的RedissonClient的Bean.
     *
     * @return
     */
    @Bean
    public RedissonClient redissonClient() {

        Config config = new Config();
        config.useSingleServer().setAddress(RedissonConstant.REDISSON_ADDRESS);
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;

    }

}
