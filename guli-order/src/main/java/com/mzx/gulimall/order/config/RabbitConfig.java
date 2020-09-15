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

    @Autowired
    private RabbitTemplate rabbitTemplate;

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
     * 消息确认机制和回退机制是在发送端进行可靠性传输的保障.
     *
     * 注解@PostConstruct作用: 将会在当前类初始化构造器调用之后执行该方法.
     *
     * @param
     */
//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//
//        // TODO: 在这里new出来的RabbitTemplate,以及在配置文件中配置的MQ配置信息是不是没有起作用.
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMandatory(true);
//        System.out.println("为RabbitTemplate配置信息成功了.");
//
//        return rabbitTemplate;
//
//    }

    @PostConstruct
    public void init(){

        System.out.println("测试注解@PostConstruct是成功执行了. ");
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);

    }

}
