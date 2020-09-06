package com.mzx.gulimall.cart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ZhenXinMa
 * @date 2020/9/6 16:06
 */
@Configuration
public class ThreadPoolConfig {

    /**
     * 该线程池的拒绝策略是: 如果可执行任务数已经满了,那么就将会在当前线程进行同步调用run方法.
     *
     * @param threadPoolProperties
     * @return
     */
    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolProperties threadPoolProperties) {

        return new ThreadPoolExecutor(threadPoolProperties.getCoolPoolSize(),
                threadPoolProperties.getMaxPoolSize(),
                threadPoolProperties.getKeepLiveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(threadPoolProperties.getMaxQueueTask()),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

    }


}
