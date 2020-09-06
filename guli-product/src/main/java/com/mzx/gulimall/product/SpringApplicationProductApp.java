package com.mzx.gulimall.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * @author ZhenXinMa
 * @date 2020/7/9 17:33
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCaching
@EnableTransactionManagement
@EnableRedisHttpSession
@ComponentScan(value = {"com.mzx"})
public class SpringApplicationProductApp {
    public static void main(String[] args) {
        
        SpringApplication.run(SpringApplicationProductApp.class,args);
    }
}
