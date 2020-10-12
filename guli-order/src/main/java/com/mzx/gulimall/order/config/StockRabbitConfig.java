package com.mzx.gulimall.order.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-12 18:01 周一.
 */
@Configuration
public class StockRabbitConfig {

    /**
     * 配置库存服务需要的交换机的信息.
     *
     * @param currentRabbitProperties 库存服务创建队列和交换机需要的配置信息(该配置信息可以从外界自行配置).
     * @return
     */
    @Bean
    public Exchange stockEventExchange(CurrentRabbitProperties currentRabbitProperties) {

        return new TopicExchange(currentRabbitProperties.getStockEventExchange(),
                currentRabbitProperties.isStockEventExchangeDurable(),
                currentRabbitProperties.isStockEventExchangeAutoDelete());

    }

    /**
     * 真正释放库存的队列,该队列其实就是一个很普通的队列.
     *
     * @param currentRabbitProperties
     * @return
     */
    @Bean
    public Queue stockReleaseStockQueue(CurrentRabbitProperties currentRabbitProperties) {

        return new Queue(currentRabbitProperties.getStockReleaseStockQueueName(),
                currentRabbitProperties.isStockEventExchangeDurable(),
                currentRabbitProperties.isStockReleaseStockQueueExclusive(),
                currentRabbitProperties.isStockEventExchangeAutoDelete(), null);

    }

    /**
     * 库存服务的延时队列的信息.
     *
     * @param currentRabbitProperties
     * @return
     */
    @Bean
    public Queue stockDelayQueue(CurrentRabbitProperties currentRabbitProperties) {

        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", currentRabbitProperties.getStockDelayQueueXDeadLetterExchange());
        arguments.put("x-dead-letter-routing-key", currentRabbitProperties.getStockDelayQueueXDeadLetterRoutingKey());
        arguments.put("x-message-ttl", currentRabbitProperties.getStockDelayQueueXMessageTtl());
        return new Queue(currentRabbitProperties.getStockDelayQueueName(),
                currentRabbitProperties.isStockEventExchangeDurable(),
                currentRabbitProperties.isStockDelayQueueExclusive(),
                currentRabbitProperties.isStockDelayQueueAutoDelete(), arguments);

    }

    /**
     * 该绑定关系将stock.event.exchange交换机和真正对库存进行解锁的队列进行绑定.
     * 路由键是stock.release.# 表示如果前缀是stock.release.的，那么就匹配不用管后缀.
     *
     * @param currentRabbitProperties
     * @return
     */
    @Bean
    public Binding stockReleaseBinding(CurrentRabbitProperties currentRabbitProperties) {

        return new Binding(currentRabbitProperties.getStockReleaseBindingDestination(),
                Binding.DestinationType.QUEUE,
                currentRabbitProperties.getStockEventExchange(),
                currentRabbitProperties.getStockReleaseBindingRoutingKey(), null);

    }


    /**
     * 将stock.event.exchange交换机和库存锁定的延时队列进行绑定.
     * 绑定的路由键是stock.locked. 该消息将会经过stock.event.exchange将其转发到延时队列.
     *
     * @param currentRabbitProperties
     * @return
     */
    @Bean
    public Binding stockLockedBinding(CurrentRabbitProperties currentRabbitProperties) {

        return new Binding(currentRabbitProperties.getStockLockedBindingDestination(),
                Binding.DestinationType.QUEUE,
                currentRabbitProperties.getStockEventExchange(),
                currentRabbitProperties.getStockLockedBindingRoutingKey(), null);

    }

}
