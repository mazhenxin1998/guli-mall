package com.mzx.gulimall.ware.config;

import com.mzx.gulimall.ware.mq.callback.ConfirmCallBack;
import com.mzx.gulimall.ware.mq.callback.ReturnCallBack;
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
 * RabbitMQ的配置文件.
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-12 17:25 周一.
 */
@Configuration
public class RabbitConfig {

    @Autowired
    private ConfirmCallBack confirmCallBack;

    @Autowired
    private ReturnCallBack returnCallBack;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {

        // 这里对rabbitTemplate进行下设置.
        rabbitTemplate.setConfirmCallback(confirmCallBack);
        rabbitTemplate.setReturnCallback(returnCallBack);

    }

    /**
     * 指定MQ发送消息的格式.
     *
     * @return
     */
    @Bean
    public MessageConverter messageConverter() {

        return new Jackson2JsonMessageConverter();

    }

    /*------------------------创建Ware服务需要的队列----------------------*/


    /**
     * 配置库存服务需要的交换机的信息.
     *
     * @param currentRabbitConfigProperties 库存服务创建队列和交换机需要的配置信息(该配置信息可以从外界自行配置).
     * @return
     */
    @Bean
    public Exchange stockEventExchange(CurrentRabbitConfigProperties currentRabbitConfigProperties) {

        return new TopicExchange(currentRabbitConfigProperties.getStockEventExchange(),
                currentRabbitConfigProperties.isStockEventExchangeDurable(),
                currentRabbitConfigProperties.isStockEventExchangeAutoDelete());

    }

    /**
     * 真正释放库存的队列,该队列其实就是一个很普通的队列.
     *
     * @param currentRabbitConfigProperties
     * @return
     */
    @Bean
    public Queue stockReleaseStockQueue(CurrentRabbitConfigProperties currentRabbitConfigProperties) {

        return new Queue(currentRabbitConfigProperties.getStockReleaseStockQueueName(),
                currentRabbitConfigProperties.isStockEventExchangeDurable(),
                currentRabbitConfigProperties.isStockReleaseStockQueueExclusive(),
                currentRabbitConfigProperties.isStockEventExchangeAutoDelete(), null);

    }

    /**
     * 库存服务的延时队列的信息.
     *
     * @param currentRabbitConfigProperties
     * @return
     */
    @Bean
    public Queue stockDelayQueue(CurrentRabbitConfigProperties currentRabbitConfigProperties) {

        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange","");
        arguments.put("x-dead-letter-routing-key","");
        arguments.put("x-message-ttl",120000);
        return new Queue(currentRabbitConfigProperties.getStockDelayQueueName(),
                currentRabbitConfigProperties.isStockEventExchangeDurable(),
                currentRabbitConfigProperties.isStockDelayQueueExclusive(),
                currentRabbitConfigProperties.isStockDelayQueueAutoDelete(), arguments);

    }

}
