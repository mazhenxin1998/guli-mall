package com.mzx.gulimall.product.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author ZhenXinMa
 * @date 2020/8/18 16:50
 */
@Controller
public class ItemController {

    @GetMapping(value = "/item.html")
    public String toItemPage(){

        return "shangpinxiangqing";
    }

}

