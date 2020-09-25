package com.mzx.gulimall.annotation;

import java.lang.annotation.*;

/**
 * 防刷接口.
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-25 20:36 周五.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLimit {

    /**
     * 含义: 在seconds秒能通过的请求数量maxCount.
     * <p>
     * Brush Prevent
     *
     * @return
     */
    int seconds();

    int maxCount();

    boolean needLogin() default false;


}