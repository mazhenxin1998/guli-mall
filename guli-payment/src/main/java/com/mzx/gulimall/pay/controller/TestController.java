package com.mzx.gulimall.pay.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-24 15:51 周六.
 */
@RestController
public class TestController {

    @GetMapping(value = "/test")
    public String t1(){

        return "SUCCESS";

    }

}
