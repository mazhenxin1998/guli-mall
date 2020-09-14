package com.mzx.gulimall.order.controller;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public void mqTest(Message message) {

        // 接收到的消息.
        System.out.println("接受到消息了");
        System.out.println("接受到的消息: " + new String(message.getBody()));
        // TODO: 现在问题来了, 我还没有进行手动ack,队列中就已经没有了? 这个是什么鬼.


    }


}
