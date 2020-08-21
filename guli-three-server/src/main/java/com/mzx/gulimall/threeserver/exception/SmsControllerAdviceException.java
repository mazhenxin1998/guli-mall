package com.mzx.gulimall.threeserver.exception;

import com.mzx.gulimall.common.utils.R;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * 使用注解RestControllerAdvice来声明捕获那个包下的异常》
 *
 * @author ZhenXinMa
 * @date 2020/8/21 16:04
 */
@RestControllerAdvice(basePackages = {"com.mzx.gulimall.threeserver.aliyun.sms.controller"})
public class SmsControllerAdviceException {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R jsr(MethodArgumentNotValidException e) {

        System.out.println("参数校验出现错误.  :  " + e);

        // 可以从该异常中获取到BindingResult
        BindingResult result = e.getBindingResult();
        // 获取到所有校验出错的字段以及原因.
        List<FieldError> fieldErrors = result.getFieldErrors();
        R r = new R();
        fieldErrors.forEach((item) -> {

            // 将所有出错的校验返回给客户端.
            r.put(item.getField(), item.getDefaultMessage());
        });

        return r;
    }

    @ExceptionHandler(value = Exception.class)
    public R exceptionHandler(Exception e) {

        return R.error(500, e.getMessage());
    }


}
