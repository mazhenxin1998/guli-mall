package com.mzx.gulimall.order.config;

import com.mzx.gulimall.order.mq.ConfirmCallback;
import com.mzx.gulimall.order.mq.ReturnCallback;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {

        SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
        containerFactory.setConnectionFactory(connectionFactory);
        return containerFactory;

    }

    /*
     * --------------------------------------------------------
     * 现在还需要为RabbitTemplate设置一些属性.
     * --------------------------------------------------------
     * */

    /**
     * 注解@PostConstruct作用: 将会在当前类初始化构造器调用之后执行该方法.
     *
     * @param connectionFactory
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        System.out.println("为RabbitTemplate配置信息成功了.");
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        return rabbitTemplate;

    }


}
