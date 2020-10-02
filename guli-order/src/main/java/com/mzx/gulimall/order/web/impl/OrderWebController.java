package com.mzx.gulimall.order.web.impl;

import com.mzx.gulimall.order.service.IOrderConfirmService;
import com.mzx.gulimall.order.service.OrderService;
import com.mzx.gulimall.order.vo.OrderConfirmVo;
import com.mzx.gulimall.order.vo.OrderSubmitVo;
import com.mzx.gulimall.order.web.IOrderWebController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

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
     * @return 返回下单成功的显示页面.
     */
    @Override
    @PostMapping(value = "/order/submit")
    public String submit(OrderSubmitVo param) {

        // TODO: 2020/10/2 先假设OrderSubmitVo能提供.

        return null;
    }


}
