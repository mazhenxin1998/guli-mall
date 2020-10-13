package com.mzx.gulimall.ware.mq.impl;

import com.mzx.gulimall.common.mq.StockLockTo;
import com.mzx.gulimall.ware.mq.StockRabbitTemplate;
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


    @Override
    public boolean lockStock(StockLockTo stockLockTo) {

        /*-------------------发送消息-----------------------------*/

        return true;

    }
}
