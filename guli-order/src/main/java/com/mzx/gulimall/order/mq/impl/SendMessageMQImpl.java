package com.mzx.gulimall.order.mq.impl;

import com.alibaba.fastjson.JSON;
import com.mzx.gulimall.order.constant.RabbitMQConstant;
import com.mzx.gulimall.order.mq.SendMessageMQ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * @author ZhenXinMa.
 * @slogan 浮生若梦, 若梦非梦, 浮生何梦?如梦之梦.
 * @create 2020-09-22 20:09 周二.
 */
@Slf4j
@Component
public class SendMessageMQImpl implements SendMessageMQ {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void send(Map<String, Object> message) {

        String jsonString = JSON.toJSONString(message);
        CorrelationData correlationData = new CorrelationData(
                UUID.randomUUID().toString());
        // TODO: 2020/10/12 如果配置了JSON转换,这里还需要对要发送的消息进行转换成JSON在发送吗?
        rabbitTemplate.convertAndSend(RabbitMQConstant.RABBIT_MQ_EXCHANGE,
                RabbitMQConstant.RABBIT_MQ_ROUTING_KEY + "1",
                jsonString,
                correlationData);
        log.info("发送了一个消息: " + jsonString);

    }
}
