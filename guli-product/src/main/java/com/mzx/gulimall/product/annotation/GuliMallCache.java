package com.mzx.gulimall.product.annotation;

import java.lang.annotation.*;

/**
 * 自定义缓存注解: 为什么要自定义一个缓存注解? SpringCache功能不能满足当前状况吗? 为什么不能满足?
 *
 * @author ZhenXinMa.
 * @slogan 滴水穿石, 不是力量大, 而是功夫深.
 * @date 2020/9/9 21:16.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GuliMallCache {

    /**
     * 表示当前方法所属缓存前缀.
     *
     * @return
     */
    String prefix() default "";

    /**
     * 为了保证缓存数据一致性,为该缓存添加一个过期时间.
     * <p>
     * 当前缓存的时间,其中是以毫秒为单位: 1000 也就是1秒.
     *
     * @return
     */
    int timeout() default 0;

    /**
     * 为了解决缓存雪崩而为key添加的
     *
     * @return
     */
    int random() default 0;

    /**
     * 为该缓存中添加的分布式锁的名称.
     *
     * @return
     */
    String lock() default "";

}
