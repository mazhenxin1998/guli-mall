package com.mzx.gulimall.oauth.web;

import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.oauth.service.LoginService;
import com.mzx.gulimall.oauth.service.RegService;
import com.mzx.gulimall.oauth.vo.MemberVo;
import com.mzx.gulimall.oauth.vo.SmsParamVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author ZhenXinMa
 * @date 2020/8/20 22:24
 */
@Controller
@RequestMapping(value = "/oauth")
public class WebController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private RegService regService;

    @GetMapping(value = "/login.html")
    public String toLoginPage() {
        return "login";
    }

    @GetMapping(value = "/reg.html")
    public String toRegPage() {

        return "reg";
    }

    @PostMapping(value = "/send/sms")
    @ResponseBody
    public R sendSms(@Valid SmsParamVo param, BindingResult bindingResult) {

        System.out.println("方法发生了.");
        return loginService.sendSms(param);
    }

    @PostMapping(value = "/user/reg")
    public R regUser(@Valid MemberVo member, BindingResult bindingResult){

        System.out.println("注册方法发生了..  参数的值: " + member.toString());
        return regService.reg(member);
    }

}
