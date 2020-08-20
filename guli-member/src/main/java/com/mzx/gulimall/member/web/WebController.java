package com.mzx.gulimall.member.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author ZhenXinMa
 * @date 2020/8/20 22:24
 */
@Controller
public class WebController {

    @GetMapping(value = "/member/login.html")
    public String toLoginPage(){
        return "login";
    }

    @GetMapping(value = "/member/reg.html")
    public String toRegPage(){

        return "reg";
    }


}
