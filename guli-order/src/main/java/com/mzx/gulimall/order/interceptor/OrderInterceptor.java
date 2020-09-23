package com.mzx.gulimall.order.interceptor;


import com.mzx.gulimall.common.constant.CurrentUserInfoConstant;
import com.mzx.gulimall.common.constant.SessionConstant;
import com.mzx.gulimall.common.constant.SpringSessionConstant;
import com.mzx.gulimall.common.model.MemberResultVo;
import com.mzx.gulimall.order.vo.UserInfoTo;
import com.mzx.gulimall.util.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author ZhenXinMa.
 * @slogan 浮生若梦, 若梦非梦, 浮生何梦?如梦之梦.
 * @create 2020-09-22 22:09 周二.
 */
@Slf4j
@Component
public class OrderInterceptor implements HandlerInterceptor {

    public static final ThreadLocal<UserInfoTo> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 统一用户线程的请求获取用户信息.
     *
     * @param request  当前用户请求.
     * @param response 当前用户响应.
     * @param handler  不知道.
     * @return 是否让当前请求通过.
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        UserInfoTo userInfoTo = new UserInfoTo();
        Object o = request.getSession().getAttribute(SpringSessionConstant.PUBLIC_USER);
        // 感觉这个能自己进行封装.
        if (StringUtils.isEmpty(o)) {

            // 用户没有登录.
            // 虽然增加了重试机制,但是userKey仍然可能为空.
            String userKey = this.getUserKey(request, response);
            if (StringUtils.isEmpty(userKey)) {

                return false;

            }

            userInfoTo.setUserKey(userKey);

        }

        MemberResultVo user = (MemberResultVo) o;
        userInfoTo.setUserId(user.getId());
        // 封装当前请求共享数据.
        THREAD_LOCAL.set(userInfoTo);
        return true;

    }

    /**
     * 从Cookie中获取user-key信息.
     * 如果不存在就增加.
     *
     * @param request  当前请求.
     * @param response 当前请求的响应.
     * @return 返回user-key的值.
     */
    private String getUserKey(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (SessionConstant.USER_KEY.equals(cookie.getName())) {

                return cookie.getValue();

            }

        }

        // 如果没有匹配Cookie, 那么就曾加一个.
        return this.addUkToCookie(response);

    }

    /**
     * 为了保证向用户Cookie中增加未登录用户的标识符, 在捕获异常块重复了异常块的执行逻辑，以增加当前方法的可用性.
     * 增加了重试机制.
     *
     * @param response
     * @return
     */
    private String addUkToCookie(HttpServletResponse response) {

        String uuid = UUID.randomUUID().toString();
        try {

            Cookie cookie = new Cookie(CurrentUserInfoConstant.USER_KEY, uuid);
            cookie.setDomain("localhost");
            cookie.setPath("/");
            cookie.setMaxAge(DateTimeUtils.getOneMonthTimeSeconds());
            response.addCookie(cookie);
            return uuid;

        } catch (Exception e) {

            // 如果出现错误, 那么就让当前请求进行休眠1.5秒之后进行重新尝试.
            try {

                Thread.sleep(1500);
                Cookie cookie = new Cookie(CurrentUserInfoConstant.USER_KEY, uuid);
                cookie.setDomain("localhost");
                cookie.setPath("/");
                cookie.setMaxAge(DateTimeUtils.getOneMonthTimeSeconds());
                response.addCookie(cookie);
                return uuid;

            } catch (InterruptedException interruptedException) {

                interruptedException.printStackTrace();
                log.error("OrderInterceptor向Cookie中添加一个新的Cookie出现异常了: ", e.getMessage());

            }

            // 如果代码还是能执行到这里,这个时候就直接返回null吧.
            System.out.println(e);
            log.error("OrderInterceptor向Cookie中添加一个新的Cookie出现异常了: ", e.getMessage());
            return null;

        }

    }
}
