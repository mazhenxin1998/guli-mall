package com.mzx.gulimall.product.service.impl;

import com.mzx.gulimall.product.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 操作用户进行登录处理一些额外的信息.
 *
 * @author ZhenXinMa
 * @date 2020/9/5 17:49
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Override
    public void addUserKey(HttpServletRequest request, HttpServletResponse response) {

        // 添加的时候应该做下判断, 如果cookie中不存在用户key，那么就存放cookie中
        // 如果cookie中存在,那么就不存放.
        boolean login = this.isLogin(request);
        if (!login) {

            // 存在,那么就不用进行存储.
            // 这里为每个用户添加一个默认的key.
            String string = UUID.randomUUID().toString();
            // cookie不能存放中文.
            Cookie cookie = new Cookie("user-key", string);
            cookie.setDomain("localhost");
            // 设置有效期.
            cookie.setMaxAge(30 * 24 * 60 * 60);
            // 将其放到cookie域中.
            response.addCookie(cookie);
            log.info("为用户默认生成一个user-key存放在cookie中. ");

        }

        //  存放了user-key, 那么就不应该进行操作.
        log.info("当前浏览器已经为用户生成了一个默认的user-key");

    }

    private boolean isLogin(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            // 如果cookie中存放了user-key， 那么就返回true.
            if ("user-key".equals(cookie.getName())) {

                return true;

            }

        }

        return false;

    }

}
