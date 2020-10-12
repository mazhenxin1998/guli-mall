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

            System.out.println("表示消息没有发送到BROKER." + correlationData.getId());

        }

    }

}
