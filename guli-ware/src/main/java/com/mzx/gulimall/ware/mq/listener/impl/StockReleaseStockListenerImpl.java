package com.mzx.gulimall.ware.mq.listener.impl;

import com.mzx.gulimall.common.mq.StockLockTo;
import com.mzx.gulimall.common.order.OrderTo;
import com.mzx.gulimall.common.order.Result;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.ware.entity.WareOrderTaskDetailEntity;
import com.mzx.gulimall.ware.entity.WareOrderTaskEntity;
import com.mzx.gulimall.ware.feign.OrderServiceFeign;
import com.mzx.gulimall.ware.mq.listener.StockReleasesStockListener;
import com.mzx.gulimall.ware.service.WareOrderTaskDetailService;
import com.mzx.gulimall.ware.service.WareOrderTaskService;
import com.mzx.gulimall.ware.service.WareSkuService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 库存自动解锁的实现逻辑.
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-12 21:10 周一.
 */
@Component
@RabbitListener(queues = "stock.release.stock.queue")
public class StockReleaseStockListenerImpl implements StockReleasesStockListener {

    @Autowired
    private OrderServiceFeign orderServiceFeign;

    @Autowired
    private WareOrderTaskService wareOrderTaskService;

    @Autowired
    private WareSkuService wareSkuService;

    @Autowired
    private WareOrderTaskDetailService wareOrderTaskDetailService;

    /**
     * 库存自动解锁逻辑.
     * <p>
     * 这个是自动解锁,但是订单服务在用户取消订单或者是时间超时了将会直接向当前队列中发送消息,但是RabbitMQ接受方法支持重载来接受一个队列中的不同消息.
     * <p>
     * 下面的情况下需要解锁:
     * 1. 订单服务远程锁库存成功,但是订单创建失败-> 需要库存自动解锁.
     * 2. 用户自行取消订单->需要库存解锁. 用户自行取消订单那么order服务将会向队列stock.release.stock.queue发送消息进行解锁.
     * 3. 订单过期没有被支付->需要库存自动解锁.
     * 4. 订单服务远程查询库存服务,库存服务在对DB做修改的时候出现异常. 虽然本地事务回滚了,但是MQ中还保存了消息.
     *
     * @param stockLockTo 要自动释放库存的订单、库存工作单、库存详情工作单.
     * @param message
     * @param channel
     * @throws IOException
     */
    @Override
    @RabbitHandler
    public void handlerAutoReleaseStockLock(StockLockTo stockLockTo, Message message, Channel channel)
            throws IOException {

        System.out.println(1);
        try {

            // 1. 先解决第四种情况: 库存服务本地锁定库存失败.
            Long stockId = stockLockTo.getId();
            // 这里应该从DB中查询下库存工作单是否存在: 解决库存服务在锁定库存的时候出现异常,导致库存没有锁，但是MQ中有释放库存的消息.
            WareOrderTaskEntity taskEntity = wareOrderTaskService.getById(stockId);
            if (taskEntity != null) {

                // 2. 对应第一种情况: order服务远程锁定库存成功，但是其创建订单失败.
                // 远程查询订单.
                Result result = orderServiceFeign.getOrderByOrderSn(stockLockTo.getOrderSn());
                // order为null表示订单在解锁库存之后创建订单失败.
                // order的状态为取消状态说明是订单超时为支付,因此需要解锁库存.
                // 在订单服务中如果订单超时未支付那么在队列order.release.order.queue中进行消息的发送.
                // 将消息直接发送到stock.release.stock.queue并进行解锁,并且还需要将订单的状态改为取消状态或者无效状态.
                // 如果用户自己手动取消了状态,那么应该解锁.
                if (result.getCode() == 0) {

                    //OrderTo order = mapTransformOrderTo(result);
                    OrderTo order = result.getOrder();
                    // 如果这样判断 order一定不为空.
                    if (order == null || order.getStatus() == 4) {

                        List<WareOrderTaskDetailEntity> detailEntities =
                                wareOrderTaskDetailService.getOrderTaskDetailsByStockId(stockId);
                        wareSkuService.listReleaseStocks(detailEntities);
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

                    }

                    //System.out.println("要去解锁库存,但是发现库存已经被解锁了: " + order.getOrderSn());
                    // TODO: 2020/10/15 如果订单是超时未付款咋弄.
                    // TODO: 2020/10/15 这里是不用做任何处理的.
                    // 如果用户主动关闭订单,应该另写一个监听队列的方法,并且参数不一样那么队列接受的消息类型就不一样.
                    // 如果用户主动关闭订单,那么订单的状态应该为取消状态.
                    // 代码能执行到这里就说明订单不为空, 并且订单的状态不为以取消状态.
                    // 那么订单的状态就有可能是完成状态、待付款状态、新建状态.

                } else {

                    Thread.sleep(1000);
                    channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);

                }

            } else {

                // 库存服务本地锁库存失败, DB中不需要对库存进行解锁, 这里只需要对MQ消息进行消费即可.
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

            }

        } catch (Exception e) {

            // 如果出现异常,可能是由于网路原因导致Feign远程请求失败.
            // 也可能是库存服务对库存释放过程出现DB问题.
            // 这个时候需要对MQ进行回滚.
            // 能捕捉到异常 要么就是网路异常导致MQ发送消息失败 要么就是DB失败.
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
            e.printStackTrace();

        }

    }

    private OrderTo mapTransformOrderTo(R result) {

        LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) result.get("data");
        if (map == null) {

            return null;

        }

        OrderTo order = new OrderTo();
        if (map.get("id") == null) {

            //如果id为空 那么返回Null表示在下订单的时候远程锁定库存成功但是创建订单失败.
            return null;

        }
        order.setOrderSn(map.get("orderSn").toString());
        order.setId(Long.valueOf(map.get("id").toString()));
        if (!StringUtils.isEmpty(map.get("memberId"))) {

            order.setMemberId(Long.valueOf(map.get("memberId").toString()));

        }

        if (!StringUtils.isEmpty(map.get("memberUsername"))) {

            order.setMemberUsername(map.get("memberUsername").toString());

        }

        if (!StringUtils.isEmpty(map.get("totalAmount"))) {

            order.setTotalAmount(new BigDecimal(map.get("totalAmount").toString()));

        }

        if (!StringUtils.isEmpty(map.get("payAmount"))) {

            order.setPayAmount(new BigDecimal(map.get("payAmount").toString()));

        }

        if (!StringUtils.isEmpty(map.get("payType"))) {

            order.setPayType(Integer.valueOf(map.get("payType").toString()));

        }

        if (!StringUtils.isEmpty(map.get("sourceType"))) {

            order.setSourceType(Integer.valueOf(map.get("sourceType").toString()));

        }

        if (!StringUtils.isEmpty(map.get("status"))) {

            order.setStatus(Integer.valueOf(map.get("status").toString()));

        }

        if (!StringUtils.isEmpty(map.get("receiverName"))) {

            order.setReceiverName(map.get("receiverName").toString());

        }

        if (!StringUtils.isEmpty(map.get("receiverPhone"))) {

            order.setReceiverPhone(map.get("receiverPhone").toString());

        }

        if (!StringUtils.isEmpty(map.get("receiverProvince"))) {

            order.setReceiverProvince(map.get("receiverProvince").toString());

        }

        if (!StringUtils.isEmpty(map.get("receiverCity"))) {

            order.setReceiverCity(map.get("receiverCity").toString());

        }

        if (!StringUtils.isEmpty(map.get("receiverRegion"))) {

            order.setReceiverRegion(map.get("receiverRegion").toString());

        }

        if (!StringUtils.isEmpty(map.get("receiverDetailAddress"))) {

            order.setReceiverDetailAddress(map.get("receiverDetailAddress").toString());

        }
        return order;

    }


    @Override
    @RabbitHandler
    public void handlerAutoReleaseOrder(OrderTo order, Message message, Channel channel) throws IOException {

        System.out.println(2);
        // 处理订单超时未支付的情况 但是库存已经扣减成功.
        try {

            // 解锁库存, 该方法要么成功,要么失败.
            wareSkuService.listReleaseStocks(order);
            // 释放库存成功, 那么就接受消息.
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {

            e.printStackTrace();
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);

        }

    }

}
