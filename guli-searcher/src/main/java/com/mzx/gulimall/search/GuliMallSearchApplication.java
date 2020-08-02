package com.mzx.gulimall.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author ZhenXinMa
 * @date 2020/7/26 20:29
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableTransactionManagement
public class GuliMallSearchApplication {

    public static void main(String[] args) {

        SpringApplication.run(GuliMallSearchApplication.class, args);
    }

}
