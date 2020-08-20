package com.mzx.gulimall.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ZhenXinMa
 * @date 2020/8/20 19:00
 */
@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolConfigProperties threadPoolConfigProperties) {

        return new ThreadPoolExecutor(threadPoolConfigProperties.getCoolPoolSize(),
                threadPoolConfigProperties.getMaxPoolSize(),
                threadPoolConfigProperties.getKeepLiveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(threadPoolConfigProperties.getMaxQueueTask()),
                new ThreadPoolExecutor.AbortPolicy());

    }

}
