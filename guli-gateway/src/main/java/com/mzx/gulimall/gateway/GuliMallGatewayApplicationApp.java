package com.mzx.gulimall.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 网关服务. 不涉及具体业务.
 * <p>
 * 由于其引入了common服务，所以在这里要排除数据源的自动配置.
 *
 * @author ZhenXinMa
 * @date 2020/7/11 21:51
 */
@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class GuliMallGatewayApplicationApp {
    public static void main(String[] args) {

        SpringApplication.run(GuliMallGatewayApplicationApp.class, args);
    }
}
