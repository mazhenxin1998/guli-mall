package com.mzx.gulimall.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-26 16:36 周六.
 */
@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolProperties threadPoolProperties) {

        // 如果输出: 150000那么表示如果配置文件配置了,那么就使用配置文件中的属性.
        // 如果输出: 100000那么表示就算配置文件中配置了相应的属性,还是使用默认的属性.
        System.out.println("maxQueueTask ： " + threadPoolProperties.getMaxPoolSize());
        // 使用同步策略.
        return new ThreadPoolExecutor(threadPoolProperties.getCoolPoolSize(),
                threadPoolProperties.getMaxPoolSize(), threadPoolProperties.getKeepLiveTime(),
                TimeUnit.SECONDS, new LinkedBlockingDeque<>(threadPoolProperties.getMaxQueueTask()),
                new ThreadPoolExecutor.CallerRunsPolicy());

    }

}
