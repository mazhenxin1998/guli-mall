package com.mzx.gulimall.ware.mq.callback;

import com.alibaba.fastjson.JSON;
import com.mzx.gulimall.ware.entity.WareUndoLogEntity;
import com.mzx.gulimall.ware.service.WareUndoLogService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-12 17:27 周一.
 */
@Component
public class ConfirmCallBack implements RabbitTemplate.ConfirmCallback {

    @Autowired
    private WareUndoLogService wareUndoLogService;

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String s) {

        if (!ack) {

            // 保证MQ消息的可靠性.
            // 虽然出现这种情况的几率很小,但是需要进行保证.
            // TODO: 2020/10/14 如果消息转发到这里,就说明该消息没有被Broker接受.
            // 为了保证消息的可靠性, 所以应该对没有发送到Broker的记录打向日志.
            System.out.println("表示消息没有发送到BROKER." + correlationData.getId());
            wareUndoLogService.rollBackLog(correlationData.getId());

        }

    }


}
