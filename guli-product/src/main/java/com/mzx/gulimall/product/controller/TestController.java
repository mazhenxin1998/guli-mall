package com.mzx.gulimall.product.controller;

import com.mzx.gulimall.annotation.AccessLimit;
import com.mzx.gulimall.annotation.CurrentCache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-25 20:56 周五.
 */
@RestController
public class TestController {

    @AccessLimit(seconds = 5, maxCount = 1, needLogin = false)
    @GetMapping(value = "/limit")
    public String limit() {

        return "OK";

    }

    @CurrentCache(prefix = "PRODUCT:CACHE:", timeout = 60, random = 10, lock = "LOCK:PRODUCT:CACHE",
            unit = TimeUnit.SECONDS)
    @AccessLimit(seconds = 1, maxCount = 1, needLogin = false)
    @GetMapping(value = "/cache/{id}")
    public String cache(@PathVariable(value = "id") String id) {

        return "OK";

    }


}
