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
     * @param s               不管ack是什么状态,该参数s一直是null......
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String s) {

        if (!ack) {

            // TODO: 2020/10/12 这里有可能会出现消息丢失的情况.
            // 如果订单没有成功发送到Broker,那么应该怎么办?
            // 一般不会出现不可路由的消息.
            // correlationData中存储的就是没有发送到Broker中的消息.
            System.out.println("订单号: " + correlationData.getId() + "在创建好订单之后给MQ发送消息的出现了故障,导致MQ没能接受到该消息.");

        }

    }

}
