package com.mzx.gulimall.order.config;

import com.mzx.gulimall.order.mq.ConfirmCallback;
import com.mzx.gulimall.order.mq.ReturnCallback;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ZhenXinMa.
 * @slogan 浮生若梦, 若梦非梦, 浮生何梦? 如梦之梦.
 * @create 2020-09-14 23:21 周一.
 */
@Configuration
public class RabbitConfig {

    @Autowired
    private ConfirmCallback confirmCallback;

    @Autowired
    private ReturnCallback returnCallback;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 配置RabbitMQ消息格式转换.
     *
     * @return
     */
    @Bean
    public MessageConverter messageConverter() {

        return new Jackson2JsonMessageConverter();

    }

    /**
     * 为RabbitMQ设置生产端消息确认机制和消息回退机制.
     */
    @PostConstruct
    public void init() {

        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);

    }

    /**
     * 创建订单自动解锁的延时队列. 切记:  延时队列是不能够被消费者进行监听的.
     * <p>
     * 只要是注入到容器中的Queue 、 Exchange 、 Binding 都会在容器启动的时候将其加入在RabbitMQ中进行创建.
     * 但是其不会覆盖创建已经存在的,就比如说在创建的时候某个属性指定了错了，仅仅修改了属性就又重新启动了当前容器，
     * 那么Queue是不会重新创建的.
     * 总之，相同名字的Queue和Exchange只会创建一次而不会重复创建.
     * <p>
     * Queue构造函数的第一个参数: 队列的名字.
     * 第二个参数: 队列是否持久化.
     * 第三个参数: 队列是否排他.
     * 第四个参数: 消息是否自动删除. ? 该消息自动删除是什么意思.
     * 第五个参数: 当前队列的一些其他配置属性.
     *
     * @return
     */
    @Bean
    public Queue orderDelayQueue(CurrentRabbitProperties currentRabbitProperties) {

        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", currentRabbitProperties.getUserOrderDelayQueueXDeadLetterExchange());
        arguments.put("x-dead-letter-routing-key",
                currentRabbitProperties.getUserOrderDelayQueueXDeadLetterRoutingKey());
        arguments.put("x-message-ttl", 60000);
        return new Queue(currentRabbitProperties.getUserOrderDelayQueueName(),
                currentRabbitProperties.isUserOrderDelayQueueDurable(),
                currentRabbitProperties.isUserOrderDelayQueueExclusive(),
                currentRabbitProperties.isUserOrderDelayQueueAutoDelete(),
                arguments);

    }

    /**
     * 对订单自动删除的队列其实就是一个很普通的队列.
     *
     * @param currentRabbitProperties
     * @return
     */
    @Bean
    public Queue orderReleaseOrderQueue(CurrentRabbitProperties currentRabbitProperties) {

        System.out.println("创建OrderReleaseOrderQueue方法执行了,方法是发生了的.");
        return new Queue(currentRabbitProperties.getOrderReleaseOrderQueueName(),
                currentRabbitProperties.isOrderReleaseOrderQueueDurable(),
                currentRabbitProperties.isOrderReleaseOrderQueueExclusive(),
                currentRabbitProperties.isOrderReleaseOrderQueueAutoDelete(), null);

    }

    /**
     * 创建Order-Event 交换机.指定类型为topic,可以根据路由键进行模糊匹配.
     *
     * @param currentRabbitProperties
     * @return
     */
    @Bean
    public Exchange userOrderDelayExchange(CurrentRabbitProperties currentRabbitProperties) {

        System.out.println("创建userOrderDelayExchange方法发生了.");
        return new TopicExchange(currentRabbitProperties.getOrderEventExchangeName(),
                currentRabbitProperties.isOrderEventExchangeDurable(),
                currentRabbitProperties.isOrderEventExchangeAutoDelete());

    }

    /*需要两个绑定关系,将上面的队列和交换机进行绑定*/

    /**
     * 当订单创建完成之后需要将该订单发送到RabbitMQ指定的交换机.
     * 而由该交换机路由到一个没有消费者监听的队列中, 而这个队列里的每个消息的存活时间为自己指定,也就是延时时间.
     * <p>
     * 延时队列和交换机进行绑定.
     *
     * @param currentRabbitProperties
     * @return
     */
    @Bean
    public Binding orderCreateOrderBinding(CurrentRabbitProperties currentRabbitProperties) {

        System.out.println("创建orderCreateOrderBinding方法发生了.");
        // 第一个参数: 表示目的地.
        // 第二个参数: 目的地类型.
        // 第三个参数: 是由哪个交换机将消息发送给目的地队列的.
        // 第四个参数: 由交换机指定routingKey进行路由.
        return new Binding(currentRabbitProperties.getOrderCreatorOrderBindingDestination(),
                Binding.DestinationType.QUEUE,
                currentRabbitProperties.getOrderCreatorOrderBindingExchangeName(),
                currentRabbitProperties.getOrderCreatorOrderBindingRoutingKey(), null);

    }

    /**
     * 订单实际自动解除的队列和交换机进行绑定.
     *
     * @param currentRabbitProperties
     * @return
     */
    @Bean
    public Binding orderReleaseOrderBinding(CurrentRabbitProperties currentRabbitProperties) {

        System.out.println("创建OrderReleaseOrderBinding方法发生了. ");
        return new Binding(currentRabbitProperties.getOrderReleaseOrderBindingDestination(),
                Binding.DestinationType.QUEUE,
                currentRabbitProperties.getOrderReleaseOrderBindingExchangeName(),
                currentRabbitProperties.getOrderReleaseOrderBindingRoutingKey(), null);

    }


}
