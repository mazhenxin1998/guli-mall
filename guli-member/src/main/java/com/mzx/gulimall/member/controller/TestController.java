package com.mzx.gulimall.member.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhenXinMa
 * @date 2020/7/10 17:32
 */
@RestController
public class TestController {

    @RequestMapping(value = "/")
    public String t1(){
        return "com.mzx.gulimall.member is success";
    }

}
