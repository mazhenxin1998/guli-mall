package com.mzx.gulimall.order.annotation;

import java.lang.annotation.*;

/**
 * 接口防刷的注解.
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-24 23:40 周四.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLimit {

    /**
     * 含义: 在seconds秒能通过的请求数量maxCount.
     *
     * Brush Prevent
     *
     * @return
     */
    int seconds();

    int maxCount();

    boolean needLogin() default false;


}
