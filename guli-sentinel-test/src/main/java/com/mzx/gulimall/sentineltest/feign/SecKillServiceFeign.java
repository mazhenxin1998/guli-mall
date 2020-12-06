package com.mzx.gulimall.sentineltest.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-30 21:53 周五.
 */
@FeignClient(value = "gulimall-seckill",
        fallback = com.mzx.gulimall.sentineltest.feign.fallback.SecKillServiceFeignFallback.class)
public interface SecKillServiceFeign {

    /**
     * 返回.
     *
     * @return
     */
    @GetMapping(value = "/get")
    String get();

}

/**
 * 针对当前Feign的回调的配置文件.
 */
class SecKillServiceFeignConfig {

    public SecKillServiceFeignFallback secKillServiceFeignFallback() {

        return new SecKillServiceFeignFallback();

    }

}

/**
 * 兜底方法.
 */
class SecKillServiceFeignFallback implements SecKillServiceFeign {

    @Override
    public String get() {

        return "兜底方法. ";

    }

}
