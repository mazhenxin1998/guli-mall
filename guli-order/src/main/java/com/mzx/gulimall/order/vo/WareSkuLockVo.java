package com.mzx.gulimall.order.vo;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-06 16:39 周二.
 */
public class WareSkuLockVo {

    /**
     * 订单序列化.
     */
    private String orderSn;

    /**
     * 需要锁定的库存.
     * 根据每一个SkuId的count数量进行SKU库存锁定.
     */
    private List<OrderItem> locks;

}
