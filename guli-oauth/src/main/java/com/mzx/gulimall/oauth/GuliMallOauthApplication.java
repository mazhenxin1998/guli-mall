package com.mzx.gulimall.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author ZhenXinMa
 * @date 2020/8/20 21:54
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GuliMallOauthApplication {

    public static void main(String[] args) {

        SpringApplication.run(GuliMallOauthApplication.class,args);
    }
}
