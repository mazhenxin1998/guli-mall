package com.mzx.gulimall.order.mq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Map;

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
    public void consumer(Message message, Channel channel) throws IOException {

        // TODO: 2020/9/23  现在还有一个问题是: 假如当前消息被消费了，但是代码还没有执行到了ack确认哪行代码服务器突然宕机了，
        //  那么其消息可能会造成消息重复消费等情况.
        // 手动ACK成功.
        System.out.println("消息消费成功了...");

    }

}
