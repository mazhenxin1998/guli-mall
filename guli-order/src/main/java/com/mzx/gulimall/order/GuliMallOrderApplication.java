package com.mzx.gulimall.order;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 注解@EnableRabbit 用于开启注解式的AMQP.//开启基于注解的RabbitMQ模式
 *
 * @author ZhenXinMa.
 * @slogan 浮生若梦, 若梦非梦, 浮生何梦? 如梦之梦.
 * @create 2020-09-14 22:11 周一.
 */
@SpringBootApplication
@EnableRabbit
@ComponentScan(basePackages = {"com.mzx.gulimall"})
@EnableFeignClients
@EnableDiscoveryClient
@EnableRedisHttpSession
public class GuliMallOrderApplication {

    public static void main(String[] args) {

        SpringApplication.run(GuliMallOrderApplication.class, args);

    }
}
