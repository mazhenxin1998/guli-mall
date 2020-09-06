package com.mzx.gulimall.oauth.web;

import com.mzx.gulimall.common.model.MemberResultVo;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.oauth.constant.RedisConstant;
import com.mzx.gulimall.oauth.service.LoginService;
import com.mzx.gulimall.oauth.service.RegService;
import com.mzx.gulimall.oauth.vo.LoginVo;
import com.mzx.gulimall.oauth.vo.MemberVo;
import com.mzx.gulimall.oauth.vo.SmsParamVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    @ResponseBody
    public R regUser(@Valid MemberVo member, BindingResult bindingResult) {

        /*
         * --------------------------------------------------------
         * 如果返回R对象,那么返回值类型应该设置为JSON.
         * --------------------------------------------------------
         * */

        System.out.println("注册方法发生了..  参数的值: " + member.toString());
        return regService.reg(member);
    }

    /**
     * 进行登录》
     *
     * @param vo
     * @return
     */
    @PostMapping(value = "/user/login")
    public String login(LoginVo vo,
                   HttpServletRequest request) {

        HttpSession session = request.getSession();
        System.out.println(1);
        MemberResultVo resultVo = loginService.login(vo);
        if (resultVo == null) {

            // 校验失败 重新返回到当前登录页面.
            return "login";
        } else {

            System.out.println("user Login 方法发生了 页面参数为: " + resultVo.toString());
            // 校验成功: 将校验结果放到session域中.
            session.setAttribute(RedisConstant.PUBLIC_USER,resultVo);
            System.out.println("SpringSession ...");
            // 重定向到首页
            System.out.println("开始重定向: 如果页面未变化 那么就说明 return出现错误. ");
            // redirect重定向会带上cookie进行重定向.
            // TODO:  为什么在页面中用window.location进行跳转获取session失败.
            return "redirect:http://localhost:10000/index.html";

        }

    }

    @GetMapping(value = "/t")
    @ResponseBody
    public String testSpringSession(HttpServletRequest request){


        HttpSession session = request.getSession();
        session.setAttribute("test","1111111111111");
        return "增加成功";
    }

    @GetMapping(value = "/get")
    @ResponseBody
    public String testSpring(HttpServletRequest request){

        HttpSession session = request.getSession();
        Object test = session.getAttribute("test");
        return test.toString();
    }


}
