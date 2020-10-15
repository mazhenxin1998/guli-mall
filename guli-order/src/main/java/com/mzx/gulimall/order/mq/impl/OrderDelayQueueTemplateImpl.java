package com.mzx.gulimall.order.mq.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.mzx.gulimall.common.order.OrderTo;
import com.mzx.gulimall.order.entity.OrderEntity;
import com.mzx.gulimall.order.mq.OrderDelayQueueTemplate;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-12 15:50 周一.
 */
@Component
public class OrderDelayQueueTemplateImpl implements OrderDelayQueueTemplate {

    final static String EXCHANGE = "order.event.exchange";

    final static String ROUTING_KEY = "order.create.order";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 向MQ发送订单解锁的请求, 如果由于网络原因导致MQ发送失败,应该怎么办?
     * MQ发送失败的情况:
     * 1. 在发送的时候由于网路异常导致发送失败.
     * 2. MQ发送到了Broker，但是由于还没有进行持久化,MQ就宕机了,导致MQ发送失败,该情况可以在发送者确认模式下得到可靠的解决.
     *
     * @param order 要解锁的订单号.
     * @return
     */
    @Override
    public boolean orderSubmit(OrderEntity order) {

        try {

            System.out.println("订单创建成功 订单向MQ order.event.exchange发送订单创建成功的消息.");
            // 消息的唯一ID和订单号的ID一样.
            CorrelationData id = new CorrelationData(order.getOrderSn());
            OrderTo orderTo = new OrderTo();
            // 使用common服务中orderTo.
            BeanUtils.copyProperties(order, orderTo);
            rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, orderTo, id);
            return true;

        } catch (Exception e) {

            // TODO: 2020/10/12 如果由于网络原因异常发生了,应该直接返回用户下单失败.
            e.printStackTrace();
            return false;

        }

    }

}
