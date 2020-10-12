package com.mzx.gulimall.order.mq.listener.impl;

import com.mzx.gulimall.order.mq.listener.OrderReleaseOrderListener;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 监听订单自动释放队列.只要里面有消息那么我就根据该订单的ID来修改该订单的状态为取消状态.
 * <p>
 * 该方法只需要监听order.release.order.queue队列即可.
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-12 16:37 周一.
 */
@Component
@RabbitListener(queues = {"order.release.order.queue"})
public class OrderReleaseOrderListenerImpl implements OrderReleaseOrderListener {

    /**
     * RabbitListener如果作用在类上,那么只要在该类下的方法上使用注解@{@link RabbitHandler}那么就说用该方法正在监听RabbitListener参数中的
     * 队列.
     */
    @Override
    @RabbitHandler
    public void handler(String orderSn, Channel channel, Message message) throws IOException {

        try {

            // 更新订单的状态.
            System.out.println("订单自动解除: " + orderSn);
            // TODO: 2020/10/12 订单自动解除的延时队列已经完成.
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {

            // 为什么这里会拒接签收消息？
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);

        }

    }

}
