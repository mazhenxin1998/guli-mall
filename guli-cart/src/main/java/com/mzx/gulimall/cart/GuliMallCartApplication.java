package com.mzx.gulimall.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author lenovo
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GuliMallCartApplication {

    public static void main(String[] args) {

        SpringApplication.run(GuliMallCartApplication.class, args);

    }

}
