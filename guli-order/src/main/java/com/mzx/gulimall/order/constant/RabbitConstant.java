package com.mzx.gulimall.order.constant;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-15 17:39 周四.
 */
public class RabbitConstant {

    public static final String STOCK_EVENT_EXCHANGE = "stock.event.exchange";

    /**
     * 向MQ队列中发送订单自动解除的消息.
     */
    public static final String ROUTING_KEY = "stock.release.order";

}
