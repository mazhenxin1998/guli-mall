package com.mzx.gulimall.order.controller;

import com.mzx.gulimall.order.mq.SendMessageMQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author ZhenXinMa
 * @date 2020/7/10 17:07
 */
@RestController
public class TestController {

    @Autowired
    private SendMessageMQ sendMessageMQ;

    @RequestMapping(value = "/")
    public String t1() {
        return "com.mzx.gulimall.order server is success";
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


}
