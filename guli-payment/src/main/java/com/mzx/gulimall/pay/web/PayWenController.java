package com.mzx.gulimall.pay.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-21 21:51 周三.
 */
@Controller
public class PayWenController {

    @GetMapping(value = "/get/orderList")
    public String orderList() {

        System.out.println("支付宝进行页面跳转.");
        return "redirect:http://localhost:26000/get/orderList";

    }

}
