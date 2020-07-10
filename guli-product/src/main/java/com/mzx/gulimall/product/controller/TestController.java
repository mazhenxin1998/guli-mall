package com.mzx.gulimall.product.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhenXinMa
 * @date 2020/7/10 16:36
 */
@RestController
public class TestController {

    @RequestMapping(value = "/")
    public String t1(){
        return "renren-genrator is SUCCESS";
    }

}
