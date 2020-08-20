package com.mzx.gulimall.product.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ZhenXinMa
 * @date 2020/8/20 18:57
 */
@Data
@ToString
@Component
@ConfigurationProperties(prefix = "guli.pool")
public class ThreadPoolConfigProperties {

    private Integer coolPoolSize = 13;
    private Integer maxPoolSize = 200;
    private Integer keepLiveTime = 10;
    private Integer maxQueueTask = 100000;

}
