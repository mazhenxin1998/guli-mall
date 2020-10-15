package com.mzx.gulimall.ware.mq.listener.impl;

import com.mzx.gulimall.common.mq.StockLockTo;
import com.mzx.gulimall.common.order.OrderTo;
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

import java.io.IOException;
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
    public void handlerAutoReleaseStockLock(StockLockTo stockLockTo, Message message, Channel channel) throws IOException {

        try {

            // 1. 先解决第四种情况: 库存服务本地锁定库存失败.
            Long stockId = stockLockTo.getId();
            // 这里应该从DB中查询下库存工作单是否存在: 解决库存服务在锁定库存的时候出现异常,导致库存没有锁，但是MQ中有释放库存的消息.
            // // TODO: 2020/10/15 下面的SQL语句会一直执行.
            WareOrderTaskEntity taskEntity = wareOrderTaskService.getById(stockId);
            if (taskEntity != null) {

                // 2. 对应第一种情况: order服务远程锁定库存成功，但是其创建订单失败.
                // 远程查询订单.
                // TODO:  这里出现问题.
                // 现在有这么一种情况: 订单没有创建成功, 那么远程查询根据路径匹配将不会匹配到.
                R result = orderServiceFeign.getOrderByOrderSn(stockLockTo.getOrderSn());
                if (result.getCode() == 0) {

                    OrderTo order = (OrderTo) result.get("data");
                    // order为null表示订单在解锁库存之后创建订单失败.
                    // order的状态为取消状态说明是订单超时为支付,因此需要解锁库存.
                    // 在订单服务中如果订单超时未支付那么在队列order.release.order.queue中进行消息的发送.
                    // 将消息直接发送到stock.release.stock.queue并进行解锁,并且还需要将订单的状态改为取消状态或者无效状态.
                    // 如果用户自己手动取消了状态,那么应该解锁.
                    if (order == null || order.getStatus() == 4) {

                        List<WareOrderTaskDetailEntity> detailEntities =
                                wareOrderTaskDetailService.getOrderTaskDetailsByStockId(stockId);
                        wareSkuService.listReleaseStocks(detailEntities);
                        System.out.println("库存解锁成功 : " + stockId);
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

                    }

                    // TODO: 2020/10/15 如果订单是超时未付款咋弄.
                    // TODO: 2020/10/15 这里是不用做任何处理的.
                    // 如果用户主动关闭订单,应该另写一个监听队列的方法,并且参数不一样那么队列接受的消息类型就不一样.
                    // 如果用户主动关闭订单,那么订单的状态应该为取消状态.
                    // 代码能执行到这里就说明订单不为空, 并且订单的状态不为以取消状态.
                    // 那么订单的状态就有可能是完成状态、待付款状态、新建状态.

                } else {

                    // feign远程请求出现异常,order服务出现异常，那么将该条消息重新入队.
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
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
            e.printStackTrace();

        }

    }

}
