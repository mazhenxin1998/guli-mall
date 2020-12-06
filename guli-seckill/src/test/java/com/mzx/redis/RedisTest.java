package com.mzx.redis;

import com.mzx.gulimall.seckill.GuliMallSecKillApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-12-01 19:18 周二.
 */
@SpringBootTest(classes = GuliMallSecKillApplication.class)
@RunWith(SpringRunner.class)
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void t1() {

        HashMap<String, String> map = new HashMap<>();
        map.put("1", "你好");
        map.put("2", "我好");
        stringRedisTemplate.opsForValue().set("test", map.toString());

        String test = stringRedisTemplate.opsForValue().get("test");
        System.out.println(1);

    }

}
