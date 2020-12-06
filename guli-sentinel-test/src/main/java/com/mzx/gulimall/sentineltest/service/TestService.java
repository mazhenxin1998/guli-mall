package com.mzx.gulimall.sentineltest.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.mzx.gulimall.sentineltest.feign.SecKillServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-30 20:01 周五.
 */
@Service
public class TestService {

    @Autowired
    private SecKillServiceFeign secKillServiceFeign;

    @SentinelResource(value = "r1")
    public String string() {

        // 在这里调用s2()为什么不是一个链路里面的吗?
        this.s2();
        // 受保护的资源的执行逻辑.
        return "你好,这里是Sentinel ..";

    }

    /**
     * 这里对s2这个资源进行链路入口进行限流.
     *
     * @return
     */
    @SentinelResource(value = "s2")
    public String s2() {

        System.out.println("Sentinel S2资源.");
        return "s2";

    }

    public String feign(){

        return secKillServiceFeign.get();

    }

}
