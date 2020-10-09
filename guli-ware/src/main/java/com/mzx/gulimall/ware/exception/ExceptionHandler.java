package com.mzx.gulimall.ware.exception;

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
 * 全局异常处理器.
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-09 20:44 周五.
 */
@RestControllerAdvice(basePackages = {"com.mzx.gulimall.ware.controller"})
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

    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    public Map handler(Exception e) {

        e.printStackTrace();
        HashMap<String, Object> map = new HashMap<>();
        map.put("库存服务捕获全局异常", e.getMessage());
        return map;

    }


}
