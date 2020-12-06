package com.mzx.gulimall.sentineltest.config;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlBlockHandler;
import com.alibaba.csp.sentinel.adapter.servlet.callback.WebCallbackManager;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-30 20:35 周五.
 */
@Configuration
public class SentinelConfiguration {

    /**
     * 自定义Sentinel流控失败后的返回.
     */
    @PostConstruct
    public void init() {

        WebCallbackManager.setUrlBlockHandler(new UrlBlockHandler() {

            @Override
            public void blocked(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                BlockException e) throws IOException {

                // TODO: 2020/10/30 自定义流控异常. 
                // 自定义对流控的响应规则.
                HashMap<String, Object> map = new HashMap<>();
                map.put("code", 200);
                map.put("msg", "Sentinel 限流自定义异常返回.");
                httpServletResponse.setContentType("text/html;charset=utf-8");
                httpServletResponse.getWriter().write(JSON.toJSONString(map));

            }

        });

    }

}
