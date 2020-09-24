package com.mzx.gulimall.order.interceptor;

import com.mzx.gulimall.common.constant.SpringSessionConstant;
import com.mzx.gulimall.common.model.MemberResultVo;
import com.mzx.gulimall.order.annotation.AccessLimit;
import com.mzx.gulimall.order.constant.RedisConstant;
import com.mzx.gulimall.util.CurrentStringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.invoke.MethodHandle;

/**
 * 实现防刷接口的拦截器.
 * <p>
 * 可以有多个拦截器,只需要配置好顺序即可》
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-24 23:42 周四.
 */
@Component
public class PreventBrushInterceptor implements HandlerInterceptor {

    /**
     * @param request  当前请求.
     * @param response 当前请求的响应.
     * @param handler  当前请求要执行的方法的句柄: 可以获取当前拦截的方法的句柄.
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 仅仅只是需要对方法请求进行拦截.
        if (handler instanceof MethodHandle) {

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {

                return true;

            }

            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            String key = request.getRequestURI();
            if (needLogin) {

                // 从session中进行判断.
                Object user = request.getSession().getAttribute(SpringSessionConstant.PUBLIC_USER);
                if (StringUtils.isEmpty(user)) {

                    // 直接返回.
                    return false;

                }

                // key是用户的ID在加上请求的URI.
                MemberResultVo member = (MemberResultVo) user;
                //key = "LIMIT:" + member.getId().toString() + key;
                key = CurrentStringUtils.append(new StringBuilder(), RedisConstant.LIMIT_PREFIX_KEY,
                        member.getId().toString(), ":", key);

            } else {

                // 如果用户未登录,那么就使用IP+端口在加上请求的URI进行key的组成.
                request.getRemoteHost();
                CurrentStringUtils.append(new StringBuilder(), RedisConstant.LIMIT_PREFIX_KEY);


            }


        }

        // 不属于方法的请求直接放行.
        return true;
    }

}
