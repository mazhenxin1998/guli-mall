package com.mzx.gulimall.seckill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 谷粒商城秒杀将会以一个定时器的任务,在并发不太高的情况下扫描近三天要上架的任务,然后进行秒杀上架.
 * 这个部分的内容待看完后面的内容之后在进行写.
 *
 * 整合Sentinel服务限流以及服务
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-26 14:32 周一.
 */
@EnableAsync
@EnableScheduling
@SpringBootApplication
@EnableDiscoveryClient
@EnableRedisHttpSession
@EnableFeignClients
public class GuliMallSecKillApplication {

    public static void main(String[] args) {

        SpringApplication.run(GuliMallSecKillApplication.class,args);

    }

}
