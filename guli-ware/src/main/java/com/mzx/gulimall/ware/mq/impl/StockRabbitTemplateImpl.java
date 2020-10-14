package com.mzx.gulimall.ware.mq.impl;

import com.mzx.gulimall.common.mq.StockLockTo;
import com.mzx.gulimall.ware.constant.StockRabbitTemplateConstant;
import com.mzx.gulimall.ware.mq.StockRabbitTemplate;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 自动触发解锁库存逻辑封装好的RabbitTemplate模板.
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-13 21:30 周二.
 */
@Component
public class StockRabbitTemplateImpl implements StockRabbitTemplate {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 现在是发送消息保证自动解锁.
     * <p>
     * 自动解锁: 需要将信息发送到stock.event.exchange的延时队列.
     *
     * @param stockLockTo
     * @return
     */
    @Override
    public boolean lockStock(StockLockTo stockLockTo) {

        // 发送消息应该保证数据的可靠性传输.
        // 利用try-catch做好RabbitMQ传输失败打入日志操作.
        try {

            CorrelationData correlationData = new CorrelationData(stockLockTo.getId().toString());
            rabbitTemplate.convertAndSend(StockRabbitTemplateConstant.EXCHANGE,
                    StockRabbitTemplateConstant.ROUTING_KEY, stockLockTo, correlationData);
            return true;

        } catch (Exception e) {

            // 如果发送失败, 应该将当前MQ发送到DB中,并且将该消息在DB中的状态为.
            System.out.println("将消息打向DB日志记录库: " + stockLockTo.toString());
            // 打印堆栈轨迹不会将当前线程暂停掉.
            e.printStackTrace();
            return false;

        }

    }

}
