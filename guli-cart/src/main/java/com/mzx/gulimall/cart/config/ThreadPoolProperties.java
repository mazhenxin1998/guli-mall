package com.mzx.gulimall.cart.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ZhenXinMa
 * @date 2020/9/6 16:08
 */
@Data
@ToString
@Component
@ConfigurationProperties(prefix = "guli.pool")
public class ThreadPoolProperties {

    private Integer coolPoolSize = 13;
    private Integer maxPoolSize = 200;
    private Integer keepLiveTime = 10;
    private Integer maxQueueTask = 100000;

}
