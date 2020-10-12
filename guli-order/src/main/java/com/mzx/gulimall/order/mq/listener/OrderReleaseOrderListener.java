package com.mzx.gulimall.order.mq.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

import java.io.IOException;

/**
 * listener一般就不用接口来显示了吧？
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-12 16:36 周一.
 */
public interface OrderReleaseOrderListener {

    /**
     * 监听订单自动释放的队列.
     * <p>
     * 只要该方法监听的队列里面有数据,那么就更新该订单的状态.
     *
     * @param orderSn 要释放订单的订单号.
     * @param channel 当前接受消息和发送消息用到的传输管道.
     * @param message Spring-AMQP 封装好的消息.
     */
    void handler(String orderSn, Channel channel, Message message) throws IOException;

}
