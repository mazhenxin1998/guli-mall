package com.mzx.gulimall.cart.exception;

import com.mzx.gulimall.common.utils.R;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 只要其指定的包中发生异常,那么就根据异常来到指定的方法内.
 *
 * @author ZhenXinMa
 * @date 2020/9/6 15:56
 */
@ControllerAdvice(basePackages = {"com.mzx.gulimall.cart.controller"})
public class ExceptionHandler {


    /**
     * 专门处理JSR303校验出现的错误.
     * <p>
     * 以JSON的形式进行返回.
     *
     * @return
     */
    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R jsr303(MethodArgumentNotValidException e) {


        BindingResult bindingResult = e.getBindingResult();
        // 将其出错的结果进行封装.
        if (bindingResult.hasErrors()) {

            // 获取出现错误的所有列.
            List<FieldError> errors = bindingResult.getFieldErrors();
            R r = new R();
            errors.stream().forEach(item -> {

                // 对出错的每一列进行处理.
                r.put(item.getField(), item.getDefaultMessage());

            });

            return r;

        }

        return R.error(10010, "向购物车中增加商品的请求参数通过JSR303进行校验的时候校验失败");

    }


    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    public R globalExceptionHandler(Exception e) {

        if (e != null) {

            return R.error(10011, e.getMessage());

        }

        return R.error(10012,"购物车模块触发了全局异常!");

    }

}
