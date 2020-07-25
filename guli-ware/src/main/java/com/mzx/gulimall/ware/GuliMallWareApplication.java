package com.mzx.gulimall.ware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author ZhenXinMa
 * @date 2020/7/25 14:42
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GuliMallWareApplication {

    public static void main(String[] args) {

        SpringApplication.run(GuliMallWareApplication.class, args);
    }

}
