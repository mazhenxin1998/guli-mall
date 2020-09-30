package com.mzx.gulimall.order.interceptor;

import com.mzx.gulimall.order.service.impl.OrderConfirmServiceImpl;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 解决Feign远程调用丢失请求头问题.
 * 只需要将其加入到IOC容器中,Feign在构造请求的时候，就自动将当前项目中的请求拦截器进行拦截,并且循环调用apply方法.
 * Calculation period
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-27 15:41 周日.
 */
@Component
public class FeignRequestInterceptor implements RequestInterceptor {


    /**
     * 其实可以通过共享HttpServletRequest来同步、异步解决Feign远程请求丢失数据问题.
     * @param template
     */
    @Override
    public void apply(RequestTemplate template) {

        // 为了解决异步, 不能直接使用RequestContextHolder直接叫进行获取.
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        template.header("Cookie",request.getHeader("Cookie"));

    }

}
