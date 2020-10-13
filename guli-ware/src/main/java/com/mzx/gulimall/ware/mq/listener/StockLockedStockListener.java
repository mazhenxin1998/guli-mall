package com.mzx.gulimall.ware.mq.listener;

import com.mzx.gulimall.common.mq.StockLockTo;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-12 20:59 周一.
 */
public interface StockLockedStockListener {

    /**
     * 向ware.event.exchange中发送消息表示我要解锁了.
     *
     * @param stockLockTo
     * @return
     */
    boolean lock(StockLockTo stockLockTo);

}
