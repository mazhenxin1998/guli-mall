package com.mzx.gulimall.order.mq.listener.impl;

import com.mzx.gulimall.common.order.OrderTo;
import com.mzx.gulimall.order.constant.RabbitConstant;
import com.mzx.gulimall.order.mq.listener.OrderReleaseOrderListener;
import com.mzx.gulimall.order.service.OrderService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private OrderService orderService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * RabbitListener如果作用在类上,那么只要在该类下的方法上使用注解@{@link RabbitHandler}那么就说用该方法正在监听RabbitListener参数中的
     * 队列.
     */
    @Override
    @RabbitHandler
    public void handler(String orderSn, Channel channel, Message message) throws IOException {

        // TODO: 2020/10/15 这个是不起作用的.
        try {

            // 更新订单的状态.
            System.out.println("订单自动解除: " + orderSn);
            // TODO: 2020/10/12 订单自动解除的延时队列已经完成.
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {

            // 为什么这里会拒接签收消息？
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);

        }

    }

    @Override
    @RabbitHandler
    public void handlerReleaseOrder(OrderTo orderTo, Channel channel, Message message) throws IOException {

        System.out.println("订单超时,自动关闭订单: " + orderTo.getOrderSn());
        try {

            // 1. 修改订单状态.
            orderService.updateOrderStatusToClose(orderTo);
            // 2. 向stock.release.stock.queue队列中发送消息.
            CorrelationData messageId = new CorrelationData(orderTo.getOrderSn());
            rabbitTemplate.convertAndSend(RabbitConstant.STOCK_EVENT_EXCHANGE, RabbitConstant.ROUTING_KEY, orderTo,
                    messageId);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {

            e.printStackTrace();
            // 如果由于在执行发送MQ消息的时候由于网络原因发生异常, 或者是由于对DB做修改出现问题, 应该拒接ack该条消息, 并且将该消息打向日志库.
            // 第二个参数为是否重新入队.
            System.out.println("向日志库增加消息成功.");
            // logService.undo(orderTo)
            System.out.println("handlerReleaseOrder 订单定时关闭向MQ发送消息的时候出现了异常.");
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);

        }

    }

}
