package com.mzx.gulimall.order.aspect;

import com.alibaba.fastjson.JSON;
import com.mzx.gulimall.order.annotation.AccessLimit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-25 17:17 周五.
 */
@Aspect
@Component
public class AccessLimitAspect {

    /**
     * 只要Controller上包含了该注解那么就能获取到.
     *
     * @return
     * @throws Throwable
     */
//    @Around(value = "@annotation(com.mzx.gulimall.order.annotation.AccessLimit)")
//    public Object around(ProceedingJoinPoint point) throws Throwable {
//
//        MethodSignature methodHandler = (MethodSignature) point.getSignature();
//        Method method = methodHandler.getMethod();
//        // 这里的注解为什么获取不到啊?  这么奇怪?
//        AccessLimit accessLimit = method.getAnnotation(AccessLimit.class);
//        // 空的?
//        int maxCount = accessLimit.maxCount();
//        int seconds = accessLimit.seconds();
//        boolean needLogin = accessLimit.needLogin();
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("code", 999);
//        map.put("message", "操作频率太快了,请稍后重试!");
//        String jsonString = JSON.toJSONString(map);
//        return jsonString;
//
//    }


}
