package com.mzx.gulimall.order.mq;

import java.util.Map;

/**
 * @author ZhenXinMa.
 * @slogan 浮生若梦, 若梦非梦, 浮生何梦?如梦之梦.
 * @create 2020-09-22 20:08 周二.
 */
public interface SendMessageMQ {

    /**
     * 以JSON的形式向MQ发送消息.
     * <p>
     * 传进来的数据是一个Map,存储以JSON进行存储.
     *
     * @param message 要发送的消息.
     */
    void send(Map<String,Object> message);


}
