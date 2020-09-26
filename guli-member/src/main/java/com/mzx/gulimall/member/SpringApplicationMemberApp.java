package com.mzx.gulimall.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author ZhenXinMa
 * @date 2020/7/9 17:32
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.mzx.gulimall"})
public class SpringApplicationMemberApp {
    public static void main(String[] args) {

        SpringApplication.run(SpringApplicationMemberApp.class, args);
    }
}
