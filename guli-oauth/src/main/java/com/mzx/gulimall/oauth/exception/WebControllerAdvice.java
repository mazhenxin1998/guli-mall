package com.mzx.gulimall.oauth.exception;

import com.mzx.gulimall.common.utils.R;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @author ZhenXinMa
 * @date 2020/8/21 22:32
 */
@RestControllerAdvice(basePackages = "com.mzx.gulimall.oauth.web")
public class WebControllerAdvice {


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R jsr(MethodArgumentNotValidException e){

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
    public R exceptionHandler(Exception e){

        // 打印异常堆栈轨迹.
        e.printStackTrace();
        System.out.println("认证服务全局异常捕获到异常: " + e.getMessage());
        return R.error(10010,e.getMessage());
    }


}
