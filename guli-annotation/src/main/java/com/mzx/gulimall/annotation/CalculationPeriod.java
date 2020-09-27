package com.mzx.gulimall.annotation;

import java.lang.annotation.*;

/**
 * 该注解不需要做任何实现,仅仅是作为一个标志.
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-27 19:43 周日.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CalculationPeriod {
}
