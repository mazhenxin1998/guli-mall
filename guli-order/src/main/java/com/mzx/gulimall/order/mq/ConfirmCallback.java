package com.mzx.gulimall.order.mq;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author ZhenXinMa.
 * @slogan 浮生若梦, 若梦非梦, 浮生何梦? 如梦之梦.
 * @create 2020-09-14 23:44 周一.
 */
@Component
public class ConfirmCallback implements RabbitTemplate.ConfirmCallback {

    /**
     * 消息确认机制.
     * <p>
     * 生产者每次发送消息, 在消息到达Broker的时候会发送给生产者一个确认的ack,表示当前Broker已经收到了当前发送的消息.
     * 如果Broker接收不到消息,那么就会返回false,表示Broker没有接受到该消息.
     * 疑问来了: 生产者是怎么知道Broker是否接受到了当前发送的消息. 如果接收到了还能确认,如果Broker没有接受到该消息,那么Broker是凭借
     * 什么依据来返回false的?
     *
     * @param correlationData 生产消息时携带的消息唯一ID. 可以是UUID.
     * @param ack             消息确认的ack，如果broker收到了消息,那么就返回true.
     * @param s
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String s) {

        if (ack) {

            System.out.println("当前消息已经被Broker接收到了: " + correlationData.getId());

        }else{

            System.err.println("当前消息没有被Broker接收到: " + correlationData.getId());

        }

    }


}
