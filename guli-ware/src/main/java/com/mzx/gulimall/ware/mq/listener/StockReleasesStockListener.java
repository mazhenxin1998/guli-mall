package com.mzx.gulimall.ware.mq.listener;

import com.mzx.gulimall.common.mq.StockLockTo;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 库存自动解锁逻辑 是被动的解锁逻辑.
 * <p>
 * 如果主动解锁逻辑失败. 那么被动的自动解锁将会是整个系统的兜底数据一致性方案.
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-12 20:59 周一.
 */
@Component
@RabbitListener(queues = "stock.release.stock.queue")
public interface StockReleasesStockListener {

    /**
     * 库存自动解锁的逻辑方案.
     *
     * @param stockLockTo 要自动释放库存的订单、库存工作单、库存详情工作单.
     * @param message
     * @param channel
     */
    void handlerAutoReleaseStockLock(StockLockTo stockLockTo, Message message, Channel channel) throws IOException;


}
