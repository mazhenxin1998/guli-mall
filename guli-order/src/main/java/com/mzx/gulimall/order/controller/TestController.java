package com.mzx.gulimall.order.controller;

import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.order.annotation.AccessLimit;
import com.mzx.gulimall.order.dao.TestDao;
import com.mzx.gulimall.order.entity.Test;
import com.mzx.gulimall.order.feign.MemberServiceFeign;
import com.mzx.gulimall.order.mq.OrderDelayQueueTemplate;
import com.mzx.gulimall.order.mq.SendMessageMQ;
import com.mzx.gulimall.order.service.OrderService;
import com.mzx.gulimall.order.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.management.MBeanServer;
import javax.servlet.http.HttpServletRequest;
import java.lang.management.*;
import java.util.HashMap;
import java.util.List;

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

    @Autowired
    private OrderDelayQueueTemplate orderDelayQueueTemplate;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TestDao testDao;

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


    /**
     * 前台传输数组以","的形式发送, SpringBoot将会默认进行分割成参数对应的数组类型.
     *
     * @param attrs
     * @return
     */
    @DeleteMapping(value = "/array/test/post")
    public String array(String[] attrs) {

        for (String attr : attrs) {

            System.out.println(attr);

        }

        return attrs.toString();

    }

    @GetMapping(value = "/test/tran")
    public Object testTransactional() {

        orderService.testTransactional();
        return "测试成功. ";

    }

    /**
     * 1. 如果主动进行捕获异常,那么Transactional是不会进行回滚的.
     * 2. 如果在方法上抛出异常，那么Transactional才是可以进行回滚的.
     *
     * @return
     */
    @Transactional
    @GetMapping(value = "/t2")
    public String t2() throws Exception {

        // 现在我在方法里面主动捕获异常. 看看数据库会不会滚.
        Test test = new Test(6L, "你好哦!");
        testDao.insert(test);
        int n = 10 / 0;
        return "ok";

    }

}
