package com.mzx.gulimall.oauth.web;

import com.mzx.gulimall.oauth.entity.MemberEntity;
import com.mzx.gulimall.oauth.service.LoginService;
import com.mzx.gulimall.oauth.vo.MemberVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 该页面仅仅用来支持页面跳转.
 * RedirectAttributes再重定向的时候支持页面之间共享数据.
 *
 * @author ZhenXinMa
 * @date 2020/8/25 16:31
 */
@Controller
@RequestMapping(value = "/oauth/skip")
public class PageSkipController {

    @Autowired
    private LoginService loginService;

    @GetMapping(value = "/to/index")
    public String toIndexPageFromRegHtml(RedirectAttributes redirectAttributes, @RequestParam(value = "userId") Long userId) {

        System.out.println("当前方法发生了. toIndexPageFromRegHtml");
        System.out.println("获取到其新增加的用户ID值为: " + userId);
        // 出现错误.
        MemberEntity memberEntity = loginService.getUserById(userId);
        redirectAttributes.addAttribute("member",memberEntity);
        // 返回首页即可.
        return "redirect:http://127.0.01:10000/";
    }


}
