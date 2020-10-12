package com.mzx.gulimall.ware.mq.callback;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 处理不可路由的消息.
 * <p>
 * 应该主要是处理一些路由不到的消息.
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-12 17:28 周一.
 */
@Component
public class ReturnCallBack implements RabbitTemplate.ReturnCallback {

    /**
     * @param message
     * @param code    退回的错误码.
     * @param s       回退原因.
     * @param s1      该消息是由那个交换机发送出来的.
     * @param s2      路由key.
     */
    @Override
    public void returnedMessage(Message message, int code, String s, String s1, String s2) {

        String body = message.getBody().toString();
        System.out.println("Message : " + body + "指定的路由Key不可达," + s2);

    }

}
