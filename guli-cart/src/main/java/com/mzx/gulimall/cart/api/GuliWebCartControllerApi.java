package com.mzx.gulimall.cart.api;

import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ZhenXinMa
 * @date 2020/9/4 7:21
 */
public interface GuliWebCartControllerApi {

    /**
     * 统一提供用户前往购物车列表.
     *
     * @param request 当前请求.
     * @param model   域数据.
     * @return
     */
    String cart(HttpServletRequest request, Model model);

}
