package com.mzx.gulimall.order.web.impl;

import com.mzx.gulimall.order.web.IOrderWebController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Order页面Controller.
 *
 * @author ZhenXinMa.
 * @slogan 浮生若梦, 若梦非梦, 浮生何梦?如梦之梦.
 * @create 2020-09-23 22:31 周三.
 */
@Controller
public class OrderWebController implements IOrderWebController {


    @Override
    @GetMapping(value = {"/", "/order.html", "/toTrade.html"})
    public String order() {

        return "order";
    }


}
