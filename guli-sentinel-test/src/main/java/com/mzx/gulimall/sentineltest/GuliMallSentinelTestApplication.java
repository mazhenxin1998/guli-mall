package com.mzx.gulimall.sentineltest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-29 18:03 周四.
 */
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class GuliMallSentinelTestApplication {

    public static void main(String[] args) {

        SpringApplication.run(GuliMallSentinelTestApplication.class,args);

    }

}
