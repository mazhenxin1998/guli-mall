package com.mzx.gulimall.member.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhenXinMa
 * @date 2020/7/11 13:37
 */
@RestController
@RequestMapping(value = "/test2/test2")
public class TestController2 {

    @RequestMapping(value = "/t2")
    public String t2(){
        return "@RequestMapping(value = \"/test/test\") 注解标注的Controller";
    }

}
