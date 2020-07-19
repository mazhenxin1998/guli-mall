package com.mzx.gulimall.order.exception;

import com.mzx.gulimall.common.utils.R;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author ZhenXinMa
 * @date 2020/7/16 15:04
 */
@RestControllerAdvice(basePackages = {"com.mzx.gulimall.member.controller"})
public class ExceptionHandler {

    /**
     * 这里处理指定异常.
     * @param e
     * @return
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {ArithmeticException.class})
    public R handler(ArithmeticException e) {

        return R.error(500, e.getMessage());
    }

    /**
     * 这里处理全局异常.
     * @param e
     * @return
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {Exception.class})
    public R handler(Exception e) {

        return R.error(e.getMessage());
    }


}
