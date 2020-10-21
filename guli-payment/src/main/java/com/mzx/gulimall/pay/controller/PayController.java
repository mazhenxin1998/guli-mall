package com.mzx.gulimall.pay.controller;

import com.alipay.api.AlipayApiException;
import com.mzx.gulimall.pay.service.PayService;
import com.mzx.gulimall.pay.vo.PaySyncVo;
import com.mzx.gulimall.pay.vo.PayVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-19 14:56 周一.
 */
@RestController
@RequestMapping(value = "/pay")
public class PayController {

    @Autowired
    @Qualifier(value = "aliPayServiceImpl")
    private PayService service;

    @GetMapping(value = "/pay.do")
    public String pay(String orderSn) throws AlipayApiException {

        PayVo vo = service.buildPayVo(orderSn);
        // service.pay返回的是支付宝支付的页面.
        return service.pay(vo);

    }

    @PostMapping(value = "/post/pay/sync/result")
    public String paySyncResult(PaySyncVo vo) {

        // 这个没有起到作用.
        // 当前系统订单状态修改成功,返回给支付宝一个确认信息.
        // 支付宝使用到了消息最终一致性解决方案.
        System.out.println(vo.toString());
        System.out.println(1);
        return "success";

    }

}
