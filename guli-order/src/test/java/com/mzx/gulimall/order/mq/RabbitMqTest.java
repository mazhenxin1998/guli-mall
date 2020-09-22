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
    public void send(){

        // 线程会乱的问题: 导致server上的日志输出到测试上.
        // 现在还是有一个问题: 没有ack的消息为什么会没有了?
        for (int i = 0; i < 20; i++) {

            System.out.println("这是第" + i + "次发送消息");
            String m = "haha";
            m += i;
            String uuid = UUID.randomUUID().toString();
            CorrelationData data = new CorrelationData(uuid);
            // 发送消息.
            rabbitTemplate.convertAndSend(RabbitMQConstant.RABBIT_MQ_EXCHANGE,
                    RabbitMQConstant.RABBIT_MQ_ROUTING_KEY, m, data);

        }

    }

}
