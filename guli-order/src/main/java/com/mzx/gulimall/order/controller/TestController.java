package com.mzx.gulimall.order.controller;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author ZhenXinMa
 * @date 2020/7/10 17:07
 */
@RestController
public class TestController {

    @RequestMapping(value = "/")
    public String t1() {
        return "com.mzx.gulimall.order server is success";
    }

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(value = "${gulimall.order.rabbitmq.queue}", durable = "true"),
                    exchange = @Exchange(value = "${gulimall.order.rabbitmq.exchange}", durable = "true", type = "direct"),
                    key = "${gulimall.order.rabbitmq.routingkey}"
            )
    })
    public void mqTest(Message message, Channel channel) {

        // 好像是该方式是不能有返回值.

        // 接收到的消息.
        System.out.println("接受到消息了");
        String s = new String(message.getBody());
        System.out.println("接受到的消息: " + s);
        // TODO: 现在问题来了, 我还没有进行手动ack,队列中就已经没有了? 这个是什么鬼.
        long tag = message.getMessageProperties().getDeliveryTag();
        try {

            if(s.length() == 5){

                // 手动进行消息确认.
                channel.basicAck(tag,false);

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }


}
