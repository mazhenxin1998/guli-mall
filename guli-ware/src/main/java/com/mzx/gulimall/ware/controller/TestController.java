package com.mzx.gulimall.ware.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhenXinMa
 * @date 2020/7/10 18:26
 */
@RestController
public class TestController {

    @RequestMapping(value = "/")
    public String t1(){
        return "com.mzx.gulimall.ware server is success";
    }

}
