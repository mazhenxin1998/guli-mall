package com.mzx.gulimall.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * RabbiMQ配置文件.
 * <p>
 * 现在有一个问题是, 为什么自动提示的字体会很小. ?
 *
 * @author ZhenXinMa.
 * @EnableConfigurationProperties 添加该注解的目的应该就是为了在写配置文件的时候能够进行自动提示吧？
 * @slogan 脚踏实地向前看.
 * @create 2020-10-11 16:37 周日.
 */
@Data
@Component
@ConfigurationProperties(prefix = "guli.rabbit")
public class CurrentRabbitProperties {

    /**
     * 订单定时关闭订单的延时队列. 该队列一定不能有任何消费者进行监听.
     */
    private String userOrderDelayQueueName = "user.order.delay.queue";

    /**
     * 订单自动关闭订单延时队列是否开启持久化的配置.
     */
    private boolean userOrderDelayQueueDurable = true;

    /**
     * 订单自动关闭订单延时队列是否对消费之进行排他. 也就是同一个时刻只能允许一个消费者进行消费.
     */
    private boolean userOrderDelayQueueExclusive = false;

    /**
     * 消息是否自动删除.
     */
    private boolean userOrderDelayQueueAutoDelete = false;

    /**
     * 消息发生死信的情况下应该将该条消息发送到那个交换机上.
     * <p>
     * 就是有一个消息在变成了死信之后路由到指定的交换机上,而该配置项就是配置的那个交换机的名称.
     */
    private String userOrderDelayQueueXDeadLetterExchange = "user.order.delay.exchange";

    /**
     * 消息发生死信之后路由到死信交换机之后需要根据当前指定的routing-key来发送到指定的队列.
     */
    private String userOrderDelayQueueXDeadLetterRoutingKey = "order";

    /**
     * 消息过期时间的单位是毫秒.
     * <p>
     * 该值默认是60000,需要自行改变的.
     */
    private String userOrderDelayQueueXMessageTtl = "60000";

    /**
     * ---------------------开始设置交换机--------------------------------------
     */

    /**
     * 订单服务需要绑定的交换机.
     * <p>
     * 该交换机既作为延时队列的交换机也作为消息处理队列的交换机,其交换机的类型是Topic.
     */
    private String orderEventExchangeName = "order.event.exchange";

    /**
     * 用户订单交换机是否开始持久化配置.
     * <p>
     * 默认为true.
     */
    private boolean orderEventExchangeDurable = true;

    /**
     * 用户订单交换机是否开启自动删除配置.
     * <p>
     * 默认为false.
     */
    private boolean orderEventExchangeAutoDelete = false;


}
