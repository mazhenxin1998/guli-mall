package com.mzx.gulimall.cart.interceptor;

import com.alibaba.fastjson.JSON;
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

            // 如果当前用户未登录,表示当前购物车状态是离线状态.
            // 这个时候就需要从cookie中查询了
            // 如果当前状态用户已经进行登录了,那么封装user-key
            // user是一个JSON对象: 属于com.mzx.gulimall.common.model.MemberResultVo
            MemberResultVo memberResultVo = JSON.parseObject(user.toString(), MemberResultVo.class);
        }

        String userKey = this.getUserKeyFromCookie(request, response);
        userInfoTo.setUserKey(userKey);


        return false;
    }


    private String getUserKeyFromCookie(HttpServletRequest request,
                                        HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            // 找出cookie中的user-key
            // user-key
            if (StringConstant.USER_COOKIE.equals(cookie.getName())) {

                // 这个将该user-key放到线程共享的ThreadLocal线程本地变量中
                // 这个userKey是一个UUID类型的字符串.
                return cookie.getValue();

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
