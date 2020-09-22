package com.mzx.gulimall.order.mq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author ZhenXinMa.
 * @slogan 浮生若梦, 若梦非梦, 浮生何梦? 如梦之梦.
 * @create 2020-09-15 22:21 周二.
 */
@Component
public class MqQueueConsumer {

    /*
     * --------------------------------------------------------
     * 以后统统使用注解@RabbitListener + @RabbitHandler来组合消费
     * 消息.
     * --------------------------------------------------------
     * */


    /**
     * 监听队指定队列中的消息.
     *
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {"${gulimall.order.rabbitmq.queue}"})
    public void consumer(Message message, Channel channel) {

        // TODO: 2020/9/22  MQ手工确认没有配置好.
        // 这个String出现了异常?
        // 现在为什么还是自动签收？
        System.out.println("接受到了消息: " + new String(message.getBody()));

    }


}
