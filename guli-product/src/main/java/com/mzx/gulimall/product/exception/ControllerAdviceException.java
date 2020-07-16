package com.mzx.gulimall.product.exception;

import com.mzx.gulimall.common.utils.R;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 只要发生指定异常处理中指定的异常类型，那么就会执行该方法，如果发生的异常没有指定的异常处理器，那么就会执行Exception异常处理器.
 * <p>
 * 先执行小的，如果小的不存在就执行大的.
 *
 * @author ZhenXinMa
 * @date 2020/7/15 18:43
 */
@RestControllerAdvice(basePackages = {"com.mzx.gulimall.product.controller"})
public class ControllerAdviceException {


    /**
     * 该异常处理器表示针对参数校验的唯一异常处理器.
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public R jsr(MethodArgumentNotValidException e) {

        // 处理异常
        BindingResult result = e.getBindingResult();

        // 获取到所有出错的字段
        List<FieldError> errors = result.getFieldErrors();
        List<Map<String, String>> resultMap = new ArrayList<>();
        // 获取整个出错的字段.
        errors.forEach(item -> {

            Map<String, String> map = new HashMap<String, String>();
            map.put(item.getField(), item.getDefaultMessage());
            map.put("提交上来的值:", item.getRejectedValue().toString());
            resultMap.add(map);
        });
        // 如果还要显示提交的字段则可以这么做

        return R.error().put("data", resultMap);
    }

    /**
     * 针对一些处理不到异常进行处理.
     *
     * @param throwable
     * @return
     */
    @ExceptionHandler(value = {Exception.class})
    public R handler(Throwable throwable) {

        return R.error();
    }

}
