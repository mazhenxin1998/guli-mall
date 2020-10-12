package com.mzx.gulimall.ware.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 如果需要使用注解@{@link ConfigurationProperties} 那么前提是当前类必须交给IOC容器管理.
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-12 17:35 周一.
 */
@Data
@ToString
@Component
@ConfigurationProperties(prefix = "guli.rabbit")
public class CurrentRabbitConfigProperties {

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
