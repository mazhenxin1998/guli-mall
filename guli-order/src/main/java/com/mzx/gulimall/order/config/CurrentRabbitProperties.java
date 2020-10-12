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
    private String userOrderDelayQueueName = "order.delay.queue";

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
    private String userOrderDelayQueueXDeadLetterExchange = "order.event.exchange";

    /**
     * 消息发生死信之后路由到死信交换机之后需要根据当前指定的routing-key来发送到指定的队列.
     */
    private String userOrderDelayQueueXDeadLetterRoutingKey = "order.release.order";

    /**
     * 消息过期时间的单位是毫秒.
     * <p>
     * 该值默认是60000,需要自行改变的.
     */
    private Integer userOrderDelayQueueXMessageTtl = 60000;

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

    /*-----------------------------------下面是orderReleaseOrderQueue的相关配置.*/

    /**
     * 订单自动删除队列的名字.
     */
    private String orderReleaseOrderQueueName = "order.release.order.queue";

    /**
     * 订单自动删除队列首付开启持久化.
     * <p>
     * 默认开启.
     */
    private boolean orderReleaseOrderQueueDurable = true;

    /**
     * 订单自动删除队列是否开启自动删除消息功能. ?
     * <p>
     * 默认为false.
     */
    private boolean orderReleaseOrderQueueAutoDelete = false;

    /**
     * 订单自动删除队列是否对消费者有排他的限制.
     * <p>
     * 有则说明消息只能被串行消费，否则能被多个消费者一起消费.
     */
    private boolean orderReleaseOrderQueueExclusive = false;

    /*---------------------------绑定关系配置------------------------*/

    /**
     * 配置交换机和队列的绑定关系.
     */
    private String orderCreatorOrderBindingDestination = "order.delay.queue";

    /**
     * 由哪个交换机和队列进行绑定.
     */
    private String orderCreatorOrderBindingExchangeName = "order.event.exchange";

    private String orderCreatorOrderBindingRoutingKey = "order.create.order";

    /**
     * 订单自动解除队列和交换机进行绑定.
     */
    private String orderReleaseOrderBindingDestination = "order.release.order.queue";

    private String orderReleaseOrderBindingExchangeName = "order.event.exchange";

    private String orderReleaseOrderBindingRoutingKey = "order.release.order";



    /*--------------------------------------配置库存消息队列-------------------------------*/
    /**
     * 自动关闭回滚库存的交换机.
     */
    private String stockEventExchange = "stock.event.exchange";

    /**
     * 交换机是否需要进行持久化.
     * <p>
     * 默认是需要的.
     */
    private boolean stockEventExchangeDurable = true;

    /**
     * 交换机是否开启自动删除模式.
     */
    private boolean stockEventExchangeAutoDelete = false;

    /*---------------------------------------配置stockReleaseStockQueue的信息-----------------------------*/

    /**
     * 真正解锁库存服务的队列.
     */
    private String stockReleaseStockQueueName = "stock.release.stock.queue";

    /**
     * 队列是否需要进行持久化.
     */
    private boolean stockReleaseStockQueueDurable = true;

    /**
     * 监听该队列的消费者的模式.
     * <p>
     * 排他表示只能有一个消费者同时进行消费.
     */
    private boolean stockReleaseStockQueueExclusive = false;

    /**
     * 自动删除.
     */
    private boolean stockReleaseStockQueueAutoDelete = false;


    /*-----------------------------------配置stock.delay.queue-------------------------------------------------------*/

    /**
     * 库存服务延时队列的名称.
     */
    private String stockDelayQueueName = "stock.delay.queue";

    private boolean stockDelayQueueDurable = true;

    private boolean stockDelayQueueExclusive = false;

    private boolean stockDelayQueueAutoDelete = false;

    private String stockDelayQueueXDeadLetterExchange = "stock.event.exchange";

    private String stockDelayQueueXDeadLetterRoutingKey = "stock.release";

    private Integer stockDelayQueueXMessageTtl = 120000;

    /*----------------------------------------------库存队列和交换机绑定的信息.*/

    /**
     * 目的队列. 真正释放库存的队列.
     */
    private String stockReleaseBindingDestination = "stock.release.stock.queue";

    private String stockReleaseBindingRoutingKey = "stock.release.#";

    private String stockLockedBindingDestination = "stock.delay.queue";

    private String stockLockedBindingRoutingKey = "stock.locked";
}
