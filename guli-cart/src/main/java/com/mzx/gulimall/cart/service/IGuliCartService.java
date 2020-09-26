package com.mzx.gulimall.cart.service;

import com.mzx.gulimall.common.utils.R;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-26 17:04 周六.
 */
public interface IGuliCartService {

    /**
     * 只要来查询该服务,那么就就说明用户一定是登录状态的.
     *
     * 查询当前用户购物车中选中的商品.
     * 当前购物车没有实现状态更换, 只要添加到了购物车,那么其状态永远为checked.
     *
     * @return
     */
    R getCheckedCartItems();
}
