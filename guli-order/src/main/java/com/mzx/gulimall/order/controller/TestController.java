package com.mzx.gulimall.order.controller;

import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.order.annotation.AccessLimit;
import com.mzx.gulimall.order.feign.MemberServiceFeign;
import com.mzx.gulimall.order.mq.SendMessageMQ;
import com.mzx.gulimall.order.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @author ZhenXinMa
 * @date 2020/7/10 17:07
 */
@RestController
public class TestController {

    @Autowired
    private SendMessageMQ sendMessageMQ;

    @Autowired
    private MemberServiceFeign memberServiceFeign;

    @GetMapping(value = "/feign/{id}")
    public Object testFeign(@PathVariable(value = "id") Long id) {

        R addr = memberServiceFeign.getAddr(id);
        return addr.get("data");

    }


    @GetMapping(value = "/send")
    public String send() {

        for (int i = 0; i < 10; i++) {

            // 这里为什么会阻塞?
            HashMap<String, Object> map = new HashMap<>();
            map.put(String.valueOf(i), String.valueOf(i));
            sendMessageMQ.send(map);

        }

        return "OK";

    }


    @GetMapping(value = "/ip")
    public String testIp(HttpServletRequest request) {

        // 111.164.195.166,123.151.77.70
        String ip = IpUtil.getIp(request);

        return ip;

    }

    @GetMapping(value = "/ip1")
    public String testIp2(HttpServletRequest request) {

        // 111.164.195.166
        String ipAddr = IpUtil.getIpAddr(request);
        return ipAddr;

    }

    /**
     * 5秒之内只允许一个请求进行访问.
     *
     * @return
     */
    @GetMapping(value = "/limit")
    @AccessLimit(seconds = 50, maxCount = 1, needLogin = false)
    public String testAccessLimit() {

        return "OK";
    }

    @GetMapping(value = "/li")
    @AccessLimit(seconds = 1, maxCount = 1, needLogin = false)
    public String limit() {

        return "OK";

    }


}
