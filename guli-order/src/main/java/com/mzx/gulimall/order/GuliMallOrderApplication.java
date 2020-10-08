package com.mzx.gulimall.order;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 注解@EnableRabbit 用于开启注解式的AMQP.//开启基于注解的RabbitMQ模式
 *
 * @author ZhenXinMa.
 * @EnableTransactionManagement 开启SpringBoot中的事务模式.
 * @EnableAspectJAutoProxy(exposeProxy = true) SpringBoot默认使用JDK动态代理来实现事务, 但是其是基于接口来实现的, 如果使用Aspectj
 * 那么其是基于类来进行代理, 并且其对外暴露对象. 如果要在一个对象的方法中调用另外一个事务中的方法, 那么就必须使用其暴露出来的代理对象进行方法之间的
 * 调用, 也就是使用AOP的AopContext.currentProxy()方法来获取当前事务方法中的代理对象.
 * @slogan 浮生若梦, 若梦非梦, 浮生何梦? 如梦之梦.
 * @create 2020-09-14 22:11 周一.
 */
@SpringBootApplication
@EnableRabbit
@ComponentScan(basePackages = {"com.mzx.gulimall"})
@EnableFeignClients
@EnableTransactionManagement
@EnableDiscoveryClient
@EnableRedisHttpSession
public class GuliMallOrderApplication {

    public static void main(String[] args) {

        SpringApplication.run(GuliMallOrderApplication.class, args);

    }
}
