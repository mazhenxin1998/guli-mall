package com.mzx.gulimall.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * @author ZhenXinMa.
 * @slogan 浮生若梦, 若梦非梦, 浮生何梦? 如梦之梦.
 * @create 2020-09-14 22:15 周一.
 */
@Configuration
public class SessionConfig {

    /**
     * 配置cookie的序列化器。
     * <p>
     * 也就是配置保存session的cookie的域的大小,为了能够让其子域进行访问.
     *
     * @return 返回默认的CookieSerializer序列化器.
     */
    @Bean
    public CookieSerializer cookieSerializer() {

        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setDomainName("localhost");
        serializer.setCookiePath("/");
        // 其实所有的字符串都不应该出现在业务逻辑中. 应该将其放在公共配置中.
        serializer.setCookieName("GULIMALL-SESSION");


        return serializer;

    }


}
