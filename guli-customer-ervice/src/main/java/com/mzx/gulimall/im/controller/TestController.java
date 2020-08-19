package com.mzx.gulimall.im.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhenXinMa
 * @date 2020/8/18 20:51
 */
@RestController
public class TestController {

    @GetMapping(value = "/t")
    public String t1(){

        return "创建成功. ";
    }

}
