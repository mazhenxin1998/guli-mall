package com.mzx.gulimall.order.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 使用ConfigurationProperties的时候如果属性有默认值, 那么在配置文件中没有配置,那么就使用默认值.
 * 如果在配置文件中配置了该属性,那么就使用配置文件中属性的值.
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-26 16:38 周六.
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
