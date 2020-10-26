package com.mzx.gulimall.seckill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-26 14:32 周一.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class GuliMallSecKillApplication {

    public static void main(String[] args) {

        SpringApplication.run(GuliMallSecKillApplication.class,args);

    }

}
