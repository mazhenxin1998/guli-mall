package com.mzx.gulimall.order.web;

import org.springframework.ui.Model;

/**
 * @author ZhenXinMa.
 * @slogan 浮生若梦, 若梦非梦, 浮生何梦?如梦之梦.
 * @create 2020-09-23 22:31 周三.
 */
public interface IOrderWebController {

    /**
     * 跳转到订单结算页面.
     * <p>
     * 需要生成订单确认的基本信息.
     * 可能包括收货人地址、要购买的商品等.
     * <p>
     * 生成订单的详细信息等.
     *
     * @return
     */
    String order(Model model);

}
