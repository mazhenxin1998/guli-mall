package com.mzx.gulimall.coupon.controller;

import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ZhenXinMa
 * @date 2020/7/10 17:55
 */
@RefreshScope
@RestController
public class TestController {

    @Value(value = "${name}")
    private String name;

    @Value(value = "${age}")
    private Integer age;

    @RequestMapping(value = "/")
    public String t1() {
        return "姓名: " + name + "已经" + age + "岁了";
    }

    @RequestMapping(value = "/gateway")
    public String t2(HttpServletRequest request) {

        String uri = request.getRequestURI();
        return uri;
    }

}
