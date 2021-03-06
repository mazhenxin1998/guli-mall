package com.mzx.gulimall.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author ZhenXinMa
 * @date 2020/7/9 17:31
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement
public class SpringApplicationCouponApp {
    public static void main(String[] args) {

        SpringApplication.run(SpringApplicationCouponApp.class, args);
    }
}
