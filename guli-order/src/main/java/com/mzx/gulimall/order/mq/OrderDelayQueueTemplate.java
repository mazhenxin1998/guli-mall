package com.mzx.gulimall.order.mq;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-12 15:50 周一.
 */
public interface OrderDelayQueueTemplate {

    /*
     * 发送消息需要保证发送的消息是可靠消息.
     * */

    /**
     * 需要传的参数: 那个订单号需要解除锁定.
     *
     * @param orderSn 要解锁的订单号.
     * @return
     */
    boolean orderSubmit(String orderSn);


}
