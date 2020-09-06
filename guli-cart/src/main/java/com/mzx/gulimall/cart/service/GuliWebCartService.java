package com.mzx.gulimall.cart.service;

import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ZhenXinMa
 * @date 2020/9/5 17:29
 */
public interface GuliWebCartService {

    /**
     * 购物车跳转操作.
     * @param request
     * @param model
     * @return
     */
    String cart(HttpServletRequest request, Model model);
}
