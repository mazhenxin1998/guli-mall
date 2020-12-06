package com.mzx.gulimall.sentineltest.controller;

import com.mzx.gulimall.sentineltest.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-29 18:12 周四.
 */
@RestController
public class TestController {

    @Autowired
    private TestService testService;

    /**
     * 对该资源进行限流.
     * <p>
     * 默认使用URI来作为当前请求资源的名字.
     * 如果没有自定义资源名称那么就是用当前请求路径作为资源的名称.
     * 如果默认使用请求路径作为资源的名字那么异常可以不进行自己指定, 直接返回的是Sentinel默认返回的异常.
     * Sentinel默认的异常为: Blocked by Sentinel (flow limiting)
     *
     * @return
     */
    @GetMapping(value = "/")
    public String string() {

        // TODO: 2020/10/30 这里为什么会抛出异常。
        // com.alibaba.csp.sentinel.slots.block.flow.FlowException: nu
        return testService.string();

    }

    @GetMapping(value = "/s")
    public String s() {

        return testService.string();

    }

    /**
     * 如果直接通过这里进行调用,那么对于链路入口限流将不会对于资源s2有效.
     *
     * @return
     */
    @GetMapping(value = "/s2")
    public String s2() {

        return testService.s2();

    }

    @GetMapping(value = "/feign")
    public String feign() {

        return testService.feign();

    }

}
