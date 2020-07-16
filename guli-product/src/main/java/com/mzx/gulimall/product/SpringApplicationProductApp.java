package com.mzx.gulimall.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author ZhenXinMa
 * @date 2020/7/9 17:33
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(value = {"com.mzx"})
public class SpringApplicationProductApp {
    public static void main(String[] args) {
        
        SpringApplication.run(SpringApplicationProductApp.class,args);
    }
}