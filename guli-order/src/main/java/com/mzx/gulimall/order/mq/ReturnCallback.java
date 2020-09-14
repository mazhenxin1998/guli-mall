package com.mzx.gulimall.order.mq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author ZhenXinMa.
 * @slogan 浮生若梦, 若梦非梦, 浮生何梦? 如梦之梦.
 * @create 2020-09-14 23:52 周一.
 */
@Component
public class ReturnCallback implements RabbitTemplate.ReturnCallback {

    /**
     * 消息回退机制.
     * <p>
     * 如果当前消息没有投递到指定的队列中,那么就回退到当前.
     *
     * @param message 回退之后的消息.
     * @param i
     * @param s
     * @param s1
     * @param s2
     */
    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {

        System.out.println("回退之后的消息: " + message);
        System.out.println("第二个参数 i " + i);
        System.out.println("第三个参数 s " + s);
        System.out.println("第四个参数 s1 " + s1);
        System.out.println("第五个参数 s2 " + s2);


    }
}
