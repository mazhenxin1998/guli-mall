package com.mzx.gulimall.order.service;

import com.mzx.gulimall.order.vo.OrderConfirmVo;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-26 10:09 周六.
 */
public interface IOrderConfirmService {
    /**
     * 查询当前用户的订单结算页面.
     *
     * @return
     */
    OrderConfirmVo queryOrderConfirm();

    /**
     * 异步查询当前用户订单结算页面.
     * @return
     */
    OrderConfirmVo queryOrderConfirmSyn();
}
