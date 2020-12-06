package com.mzx.gulimall.seckill.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-26 15:10 周一.
 */
@RestController
public class TestController {

    @GetMapping(value = "/")
    public String index(){

        return "http://localhost:28000/谷粒商城-秒杀服务.";

    }

    @GetMapping(value = "/get")
    public String get(){

        return "seckill";

    }


}
