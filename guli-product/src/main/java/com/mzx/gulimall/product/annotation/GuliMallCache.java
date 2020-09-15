package com.mzx.gulimall.product.annotation;

import javax.validation.constraints.NotNull;
import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 自定义缓存注解: 为什么要自定义一个缓存注解? SpringCache功能不能满足当前状况吗? 为什么不能满足?
 *  SpringCache在分布式环境下是不能满足的,由于缓存击穿问题,所以说需要使用分布式锁对其进行同步控制.
 * <p>
 * 缓存的key默认是第一个参数.
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
     * 表示当前方法所属缓存前缀.这个不提供默认值.
     *
     * @return
     */
    @NotNull String prefix();

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

    /**
     * 如果当前方法只缓存一个数据,那么就指定自己的key(该key是前缀在加上当前的key).
     * 如果说当前缓存方法根据参数来缓存不同的数据,那么就使用前缀后面添加参数的第一个参数值来进行缓存.
     *
     * @return
     */
    String key() default "";

    TimeUnit unit();

}
