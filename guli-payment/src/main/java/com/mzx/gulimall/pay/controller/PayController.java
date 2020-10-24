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

import javax.servlet.http.HttpServletRequest;

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

    /**
     * 这里的异常是需要被捕获到的. 
     * @param vo
     * @param request
     * @return
     * @throws AlipayApiException
     */
    @PostMapping(value = "/post/pay/sync/result")
    public String paySyncResult(PaySyncVo vo, HttpServletRequest request) throws AlipayApiException {

        System.out.println("pay sync result start...");
        return service.doPaySyncResult(vo,request);

    }

}
