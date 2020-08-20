package com.mzx.gulimall.oauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhenXinMa
 * @date 2020/8/20 22:05
 */
@RestController
public class TestController {

    @GetMapping(value = "/")
    public String test(){

        return  "success";
    }

}
