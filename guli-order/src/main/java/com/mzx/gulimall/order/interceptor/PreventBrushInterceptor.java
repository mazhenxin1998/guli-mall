package com.mzx.gulimall.order.interceptor;

import com.alibaba.fastjson.JSON;
import com.mzx.gulimall.common.constant.SpringSessionConstant;
import com.mzx.gulimall.common.model.MemberResultVo;
import com.mzx.gulimall.order.annotation.AccessLimit;
import com.mzx.gulimall.order.constant.RedisConstant;
import com.mzx.gulimall.order.service.IAccessLimitService;
import com.mzx.gulimall.order.util.IpUtil;
import com.mzx.gulimall.util.CurrentStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 实现防刷接口的拦截器.
 * <p>
 * 可以有多个拦截器,只需要配置好顺序即可.
 * <p>
 * 获取不到注解.
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-24 23:42 周四.
 */
@Component
public class PreventBrushInterceptor implements HandlerInterceptor {

    private ThreadLocal<String> local = new ThreadLocal<>();

    @Autowired
    private IAccessLimitService iAccessLimitService;

    /**
     * @param request  当前请求.
     * @param response 当前请求的响应.
     * @param handler  当前请求要执行的方法的句柄: 可以获取当前拦截的方法的句柄.
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            AccessLimit accessLimit = method.getAnnotation(AccessLimit.class);
            if (accessLimit == null) {

                return true;

            }

            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            String key = request.getRequestURI();
            // b为true,那么就return.
            boolean b = this.getKey(request, needLogin, key);
            if (!b) {

                // 表示当前注解标注的方法需要用户登录之后才能访问,但是现在用户没有进行登录就能直接访问.
                // 可以使用Response响应给客户端.
                return false;
            }

            // 重新获取到key,只要代码能执行到这里,就一定获取到值.
            key = local.get();
            String value = iAccessLimitService.getValue(key);
            if (StringUtils.isEmpty(value)) {

                // 表示当前不进行限制 用户可以直接访问.
                // 但是要在Redis中记录一下.
                iAccessLimitService.setKeyWithExpiration(key, 1, seconds);
                return true;

            }

            iAccessLimitService.keyIncrement(key);
            // 如果查询出来的value不是空 那么就需做进一步的判断.
            String v = iAccessLimitService.getValue(key);
            if (StringUtils.isEmpty(v)) {

                return true;

            }

            if (Integer.parseInt(v) > maxCount) {

                // 表示已经限流了.
                // 需要给用户进行响应.
                Map<String, Object> map = new HashMap<>();
                map.put("code", 499);
                map.put("message", "对不起,您操作太快了 请稍后重试!");
                String jsonString = JSON.toJSONString(map);
                response.setContentType("text/json;charset=utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(jsonString);
                return false;

            }

        }

        // 不属于方法的请求直接放行.
        return true;

    }

    /**
     * 只要该方法只能执行,
     *
     * @param request
     * @param needLogin
     * @param key
     * @return
     */
    private boolean getKey(HttpServletRequest request, boolean needLogin, String key) {

        if (needLogin) {

            // 从session中进行判断.
            Object user = request.getSession().getAttribute(SpringSessionConstant.PUBLIC_USER);
            if (StringUtils.isEmpty(user)) {

                // 注解中需要用户进行登录,但是用户没有登录就请求了接口,应该直接返回给用户让其直接进行登录.
                return false;

            }

            // key是用户的ID在加上请求的URI.
            MemberResultVo member = (MemberResultVo) user;
            //key = "LIMIT:" + member.getId().toString() + ":" + key;
            key = CurrentStringUtils.append(new StringBuilder(), RedisConstant.LIMIT_PREFIX_KEY,
                    member.getId().toString(), ":", key);

        } else {

            // 如果用户未登录,那么就使用IP+端口在加上请求的URI进行key的组成.
            request.getRemoteHost();
            key = CurrentStringUtils.append(new StringBuilder(), RedisConstant.LIMIT_PREFIX_KEY,
                    IpUtil.getIpAddr(request), ":", key);

        }

        local.set(key);
        return true;

    }

}
