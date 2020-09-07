package com.mzx.gulimall.cart.service;

import com.mzx.gulimall.cart.vo.CartParamVo;
import com.mzx.gulimall.common.model.Cart;
import com.mzx.gulimall.common.model.CartItem;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ZhenXinMa
 * @date 2020/9/5 17:29
 */
public interface GuliWebCartService {

    /**
     * 对购物车上的数据进行封装操作.
     *
     * @param request
     * @param model
     * @param isLogin 表示当前是否登录: true表示登录状态.
     * @return
     */
    Cart cart(HttpServletRequest request, Model model, boolean isLogin);

    /**
     * 向购物车增加商品.
     *
     * @param request
     * @param param
     * @param model
     * @return
     */
    CartItem add(HttpServletRequest request, CartParamVo param, Model model);

}
