package com.mzx.gulimall.order.exception;

import com.alibaba.fastjson.JSON;
import com.mzx.gulimall.common.utils.R;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZhenXinMa
 * @date 2020/7/16 15:04
 */
@RestControllerAdvice(basePackages = {"com.mzx.gulimall.order.controller", "com.mzx.gulimall.order.web.impl"})
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handler(MethodArgumentNotValidException e) {

        e.printStackTrace();
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
        return R.error().put("data", JSON.toJSONString(resultMap));

    }


    /**
     * 这里处理指定异常.
     * <p>
     * ArithmeticException是什么异常.?
     *
     * @param e
     * @return
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {ArithmeticException.class})
    public R handler(ArithmeticException e) {

        e.printStackTrace();
        return R.error(500, e.getMessage());

    }

    /**
     * 这里处理全局异常.
     *
     * @param e
     * @return
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {Exception.class})
    public R handler(Exception e) {

        // 应该抛出异常.
        e.printStackTrace();
        return R.error(e.getMessage());

    }


}
