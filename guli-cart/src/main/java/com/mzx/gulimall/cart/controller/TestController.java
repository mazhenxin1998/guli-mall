package com.mzx.gulimall.cart.controller;

import com.mzx.gulimall.cart.annotation.CalculationPeriod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.TimeUnit;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-27 20:05 周日.
 */
@RestController
public class TestController {

    @CalculationPeriod
    @GetMapping(value = "/t")
    public String t1(){

        try {

            Thread.sleep(1200);

        } catch (InterruptedException e) {

            e.printStackTrace();

        }

        return "OK";

    }

}
