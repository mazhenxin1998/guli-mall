package com.mzx.gulimall.pay.service.impl;

import com.alipay.api.AlipayApiException;
import com.mzx.gulimall.common.order.OrderTo;
import com.mzx.gulimall.common.order.Result;
import com.mzx.gulimall.pay.feign.OrderServiceFeign;
import com.mzx.gulimall.pay.service.PayService;
import com.mzx.gulimall.pay.template.PayTemplate;
import com.mzx.gulimall.pay.vo.PayVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-19 15:00 周一.
 */
@Service(value = "aliPayServiceImpl")
public class AliPayServiceImpl implements PayService {

    @Autowired
    @Qualifier(value = "payTemplateImpl")
    private PayTemplate payTemplate;

    @Autowired
    private OrderServiceFeign orderServiceFeign;


    @Override
    public String pay(PayVo vo) {

        try {

            // 支付订单之前需要检查当前订单是否过期.
            // 直接进行支付.
            return payTemplate.pay(vo);

        } catch (AlipayApiException e) {

            e.printStackTrace();
            return "调用AliPay出现异常.";

        }

    }

    @Override
    public PayVo buildPayVo(String orderSn) {

        PayVo vo = new PayVo();
        Result feignResult = orderServiceFeign.getOrderByOrderSn(orderSn);
        OrderTo order = feignResult.getOrder();
        vo.setOut_trade_no(orderSn);
        vo.setSubject("小苹果校园超市助手支付页面:");
        // 支付金额保留两位小数,向上取整.
        vo.setTotal_amount(order.getPayAmount().setScale(2, RoundingMode.UP).toString());
        vo.setBody("该用户很懒,下单是没有进行备注.!");
        return vo;

    }

}
