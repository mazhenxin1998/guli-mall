package com.mzx.gulimall.cart.interceptor;

import com.mzx.gulimall.cart.constant.RedisSessionConstant;
import com.mzx.gulimall.cart.constant.StringConstant;
import com.mzx.gulimall.cart.vo.UserInfoTo;
import com.mzx.gulimall.common.model.MemberResultVo;
import com.mzx.gulimall.util.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * @author ZhenXinMa
 * @date 2020/9/5 18:08
 */
@Slf4j
@Component
public class CartInterceptor implements HandlerInterceptor {

    /**
     * 用户当前线程共享: 且存放在线程的工作内存空间之中, 各个线程之间互不干扰.
     * 共享用户身份的线程本地变量.
     * ThreadLocal需要掌握原理以及内存泄漏.
     * 解决内存泄漏: 每次使用之后应该调用remove方法.
     * 这里所说的使用应该是当前线程最后一次使用ThreadLocal对象.
     */
    public static volatile ThreadLocal<UserInfoTo> local = new ThreadLocal<>();

    /**
     * 在用户查询购物车列表之前,
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        UserInfoTo userInfoTo = new UserInfoTo();
        // 先判断session中是否有当前状态的用户.
        HttpSession session = request.getSession();
        Object user = session.getAttribute(RedisSessionConstant.PUBLIC_USER);
        if (!StringUtils.isEmpty(user)) {

            // 如果当前状态用户已经进行登录了,那么也封装user-key
            // 存储的时候没有存储为JSON格式.
            MemberResultVo memberResultVo = (MemberResultVo) user;
            userInfoTo.setUserId(memberResultVo.getId());

        }

        // 不管用户是否登录,现在都需要将user-key存放在ThreadLocal中. 但是需要注意的是，这个userKey可能返回null
        // 虽然几率很小,但是还是需要确定的.
        String userKey = this.getUserKeyFromCookie(request, response);
        if (StringUtils.isEmpty(userKey)) {

            // 如果下面向cookie中增加信息失败了,那么返回的值是null.
            return false;

        }

        userInfoTo.setUserKey(userKey);
        // 共享变量.
        local.set(userInfoTo);
        return true;

    }


    private String getUserKeyFromCookie(HttpServletRequest request,
                                        HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {

            for (Cookie cookie : cookies) {

                // 找出cookie中的user-key
                // user-key
                if (StringConstant.USER_COOKIE.equals(cookie.getName())) {

                    // 这个将该user-key放到线程共享的ThreadLocal线程本地变量中
                    // 这个userKey是一个UUID类型的字符串.
                    return cookie.getValue();

                }

            }

        }

        // 如果没有从cookie中找到,那么就向cookie中存入一个userKey,并且返回. 但是需要注意的是该返回值有可能是空的.
        return this.addCookieToWebStorage(response);

    }

    private String addCookieToWebStorage(HttpServletResponse response) {

        String uuid = UUID.randomUUID().toString();
        try {

            // 增加一个cookie.
            Cookie cookie = new Cookie(StringConstant.USER_COOKIE, uuid);
            cookie.setDomain("localhost");
            // 默认设置一个月的过期时间.
            cookie.setMaxAge(DateTimeUtils.getOneMonthTimeSeconds());
            response.addCookie(cookie);
            return uuid;

        } catch (Exception e) {

            log.error("再检查请求所带来的cookie中发现没有user-key的时候，新增一个cookie的时候出现了异常 : ", e);
            return null;

        }

    }

}
