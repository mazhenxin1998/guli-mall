package com.mzx.gulimall.order.web;

import com.mzx.gulimall.order.vo.OrderSubmitVo;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

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
     * @param model   请求域.
     * @param request 当前请求.
     * @return 返回订单页面.
     */
    String order(HttpServletRequest request, Model model);

    /**
     * 提交信息,生成订单.
     *
     * @param param 当前请求的所有信息.
     * @return 返回下单成功的逻辑页面.
     */
    String submit(OrderSubmitVo param);

}
