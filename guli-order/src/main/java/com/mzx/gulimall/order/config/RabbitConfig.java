package com.mzx.gulimall.order.config;

import com.mzx.gulimall.order.mq.ConfirmCallback;
import com.mzx.gulimall.order.mq.ReturnCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author ZhenXinMa.
 * @slogan 浮生若梦, 若梦非梦, 浮生何梦? 如梦之梦.
 * @create 2020-09-14 23:21 周一.
 */
@Configuration
public class RabbitConfig {

    @Autowired
    private ConfirmCallback confirmCallback;

    @Autowired
    private ReturnCallback returnCallback;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 为RabbitMQ设置生产端消息确认机制和消息回退机制.
     */
    @PostConstruct
    public void init() {

        System.out.println("测试注解@PostConstruct是成功执行了. ");
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);

    }

}
