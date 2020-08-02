package com.mzx.gulimall.coupon.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhenXinMa
 * @date 2020/7/26 13:32
 */
@RestController
public class TestController {


    @RequestMapping(value = "/test/t1")
    public String t1(){
        System.out.println("coupon 中的test t1 执行了");
        // 假设在服务进行远程调用的时候出现了异常.
        throw new RuntimeException();
    }


}
