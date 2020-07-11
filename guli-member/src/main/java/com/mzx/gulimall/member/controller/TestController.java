package com.mzx.gulimall.member.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhenXinMa
 * @date 2020/7/10 17:32
 */
@RestController
@RequestMapping(value = "test1/tt")
public class TestController {

    @RequestMapping(value = "/t1")
    public String t1(){
        return "@RequestMapping(value = \"test/test\")注解标注的Controller";
    }

}
