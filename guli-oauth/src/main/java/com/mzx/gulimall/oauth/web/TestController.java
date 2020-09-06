package com.mzx.gulimall.oauth.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author ZhenXinMa
 * @date 2020/8/26 18:56
 */
@RestController
public class TestController {

    @GetMapping(value = "/add")
    public String addSession(HttpServletRequest request){

        HttpSession session = request.getSession();
        session.setAttribute("test","test");
        return "增加成功";
    }

    @GetMapping(value = "/get")
    public String getSession(HttpServletRequest request){

        HttpSession session = request.getSession();
        Object test = session.getAttribute("test");
        return test.toString();
    }


}
