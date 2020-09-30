package com.mzx.gulimall.order.web.impl;

import com.mzx.gulimall.order.service.IOrderConfirmService;
import com.mzx.gulimall.order.vo.OrderConfirmVo;
import com.mzx.gulimall.order.web.IOrderWebController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Order页面Controller.
 *
 * @author ZhenXinMa.
 * @slogan 浮生若梦, 若梦非梦, 浮生何梦?如梦之梦.
 * @create 2020-09-23 22:31 周三.
 */
@Controller
public class OrderWebController implements IOrderWebController {

    @Autowired
    private IOrderConfirmService iOrderConfirmService;

    @Override
    @GetMapping(value = {"/", "/order.html", "/toTrade.html"})
    public String order(HttpServletRequest request, Model model) {

        long start = System.currentTimeMillis();
        OrderConfirmVo confirmVo = iOrderConfirmService.queryOrderConfirmSyn();
        if (confirmVo == null) {

            String originUrl = "http://localhost:26000" + request.getRequestURI();
            return "redirect:http://localhost:24000/oauth/login.html?origin_url=" + originUrl;

        }

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        confirmVo.setToken(uuid);
        long end = System.currentTimeMillis();
        System.out.println("方法 com.mzx.gulimall.order.web.impl.order(Model model) 中执行queryOrderConfirmSyn()耗费了时间" +
                " " + (end - start) + "毫秒.");
        model.addAttribute("confirm", confirmVo);
        return "order";

    }


}
