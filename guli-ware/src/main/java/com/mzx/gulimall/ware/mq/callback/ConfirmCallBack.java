package com.mzx.gulimall.ware.mq.callback;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-12 17:27 周一.
 */
@Component
public class ConfirmCallBack implements RabbitTemplate.ConfirmCallback {

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String s) {

        if (!ack) {

            // TODO: 2020/10/14 如果消息转发到这里,就说明该消息没有被Broker接受.
            // 为了保证消息的可靠性, 所以应该对没有发送到Broker的记录打向日志.
            System.out.println("消息发送向Broker发送失败. 打向DB日志库.");
            System.out.println("表示消息没有发送到BROKER." + correlationData.getId());

        }

    }

}
