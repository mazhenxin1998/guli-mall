package com.mzx.gulimall.gateway.config;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZhenXinMa
 * @date 2020/7/16 13:32
 */
public class JsonErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {


    /**
     * 基于响应式编程风格.
     * @param errorAttributes
     * @return
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {

        // 将相应信息切换成JSON
        // 方法引用.
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    /**
     * 具体的错误显示信息的定制,该方法返回的Map就是其要显示的JSON信息.
     *
     * @param request
     * @param includeStackTrace
     * @return
     */
    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {

        // 503 表示下游服务不可用
        // 如果是404则让他跳转到404页面
        // 这里其实可以根据异常类型进行定制化逻辑
        Throwable error = super.getError(request);
        Map<String, Object> errorAttributes = new HashMap<>(8);
        // 通过message获取到指定错误码
        String message = error.getMessage();
        // 通过该方式获取到的状态码信息可能不是正规.
        String[] s = message.split(" ");
        int code = Integer.parseInt(s[0]);
        errorAttributes.put("message", error.getMessage());
        errorAttributes.put("code", code);
        errorAttributes.put("method", request.methodName());
        errorAttributes.put("path", request.path());
        return errorAttributes;
    }

    @Override
    protected HttpStatus getHttpStatus(Map<String, Object> errorAttributes) {

        // 这里其实可以根据errorAttributes里面的属性定制HTTP响应码
        int code = (int) errorAttributes.get("code");
        return HttpStatus.valueOf(code);
    }

    public JsonErrorWebExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties,
                                        ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }
}
