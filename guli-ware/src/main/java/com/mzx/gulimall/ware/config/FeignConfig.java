package com.mzx.gulimall.ware.config;

import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 参考SpringCloud微服务入门实战Feign配置.
 * <p>
 * 如果使用配置类进行配置, 那么配置文件中的相关配置可能就没有起作用.
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-09 19:29 周五.
 */
@Configuration
public class FeignConfig {

    /**
     * 对Feign做的远程请求时间限制的配置.
     *
     * @return
     */
    @Bean
    public Request.Options options() {

        /*
        * 第一个参数是: 连接超时时间.
        * 第二个参数是: 取超时时间.
        * */
        return new Request.Options(5000, 10000);
    }


}
