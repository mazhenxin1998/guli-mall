package com.mzx.gulimall.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author ZhenXinMa.
 * @slogan 浮生若梦, 若梦非梦, 浮生何梦? 如梦之梦.
 * @create 2020-09-15 20:54 周二.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GuliMallPaymentApplication {

    public static void main(String[] args) {

        SpringApplication.run(GuliMallPaymentApplication.class,args);

    }

}
