package com.mzx.gulimall.im;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author ZhenXinMa
 * @date 2020/8/18 20:48
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GuliMallImServerApplication {

    public static void main(String[] args) {

        SpringApplication.run(GuliMallImServerApplication.class,args);
    }

}
