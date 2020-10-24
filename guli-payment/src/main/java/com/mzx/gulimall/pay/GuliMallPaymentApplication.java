package com.mzx.gulimall.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 支付模块现在需要额外另起一个服务吗 淘宝等大项目中应该是需要将支付模块额外另起一个模块.?
 * <p>
 * 该模块是需要事务的.
 *
 * @author ZhenXinMa.
 * @slogan 浮生若梦, 若梦非梦, 浮生何梦? 如梦之梦.
 * @create 2020-09-15 20:54 周二.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableFeignClients
public class GuliMallPaymentApplication {

    public static void main(String[] args) {

        SpringApplication.run(GuliMallPaymentApplication.class, args);

    }

}
