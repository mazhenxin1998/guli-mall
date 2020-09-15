package com.mzx.gulimall.order.mq;

import com.mzx.gulimall.order.GuliMallOrderApplication;
import com.mzx.gulimall.order.constant.RabbitMQConstant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

/**
 * @author ZhenXinMa.
 * @slogan 浮生若梦, 若梦非梦, 浮生何梦? 如梦之梦.
 * @create 2020-09-15 00:06 周二.
 */
@SpringBootTest(classes = GuliMallOrderApplication.class)
@RunWith(SpringRunner.class)
public class RabbitMqTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void sendMessage() {

        // 模拟发送消息.
        // 现在发送的消息使用的是JDK序列化机制
        String m = "haha5";
        for (int i = 0; i < 5; i++) {

            String uuid = UUID.randomUUID().toString();
            CorrelationData data = new CorrelationData(uuid);
            // 发送消息.
            rabbitTemplate.convertAndSend(RabbitMQConstant.RABBIT_MQ_EXCHANGE,
                    RabbitMQConstant.RABBIT_MQ_ROUTING_KEY, m, data);

        }

        for (int i = 0; i < 5; i++) {

            String uuid = UUID.randomUUID().toString();
            CorrelationData data = new CorrelationData(uuid);
            // 发送消息.
            rabbitTemplate.convertAndSend(RabbitMQConstant.RABBIT_MQ_EXCHANGE,
                    RabbitMQConstant.RABBIT_MQ_ROUTING_KEY, m + "aa", data);


        }

    }


}
