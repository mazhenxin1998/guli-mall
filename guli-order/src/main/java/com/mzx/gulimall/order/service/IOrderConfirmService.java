package com.mzx.gulimall.order.service;

import com.mzx.gulimall.order.vo.OrderConfirmVo;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

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
     *
     * @return
     */
    OrderConfirmVo queryOrderConfirmSyn();

    /**
     * 将Controller中的业务逻辑在Service中.
     *
     * @param request
     * @param model
     * @return
     */
    String order(HttpServletRequest request, Model model);
}
