package com.mzx.gulimall.ware.mq;

import com.mzx.gulimall.common.mq.StockLockTo;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-13 21:30 周二.
 */
public interface StockRabbitTemplate {

    /**
     * 向stock.event.exchange锁定库存服务的延伸队列发送消息表示我这个消息要在一段时间之后进行解锁.
     *
     * @param stockLockTo
     * @return
     */
    boolean lockStock(StockLockTo stockLockTo);

}
