package com.mzx.gulimall.order.web.impl;

import com.mzx.gulimall.order.annotation.AccessLimit;
import com.mzx.gulimall.order.service.IOrderConfirmService;
import com.mzx.gulimall.order.service.OrderService;
import com.mzx.gulimall.order.vo.OrderSubmitVo;
import com.mzx.gulimall.order.web.IOrderWebController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Order页面Controller.
 * <p>
 * 有地方需要改善: 不应该在Controller里面写业务逻辑, 应该保持Controller的简洁性并且应该将在Controller的逻辑放在Service中.
 *
 * @author ZhenXinMa.
 * @slogan 浮生若梦, 若梦非梦, 浮生何梦?如梦之梦.
 * @create 2020-09-23 22:31 周三.
 */
@Controller
public class OrderWebController implements IOrderWebController {

    @Autowired
    private IOrderConfirmService iOrderConfirmService;

    @Autowired
    private OrderService orderService;

    @Override
    @GetMapping(value = {"/", "/order.html", "/toTrade.html"})
    public String order(HttpServletRequest request, Model model) {

        return iOrderConfirmService.order(request, model);

    }

    /**
     * 提交信息,生成订单.
     *
     * 当前接口一定需要保证接口的幂等.
     * 1. 通过自定义注解@AccessLimit来进行接口防刷.
     * 2. 通过使用token机制来保证当前接口的幂等性.
     *
     * @return 返回下单成功的显示页面.
     */
    @Override
    @AccessLimit(seconds = 1,maxCount = 1,needLogin = false)
    @GetMapping(value = "/order/submit")
    public String submit(@Valid OrderSubmitVo param,HttpServletRequest request) {

        // TODO: 2020/10/4 现在需要确定的是当前接口是否需要用户登录.
        return orderService.submit(param, request);

    }


}
