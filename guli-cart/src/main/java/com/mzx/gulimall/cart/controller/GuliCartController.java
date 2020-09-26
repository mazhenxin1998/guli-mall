package com.mzx.gulimall.cart.controller;

import com.mzx.gulimall.cart.service.IGuliCartService;
import com.mzx.gulimall.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-26 17:04 周六.
 */
@RestController
public class GuliCartController {

    @Autowired
    private IGuliCartService iGuliCartService;

    @GetMapping(value = "/cart/get/checked/items")
    public R getCheckedCartItems(){

        return iGuliCartService.getCheckedCartItems();

    }



}
