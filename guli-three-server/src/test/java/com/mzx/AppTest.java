package com.mzx;

import static org.junit.Assert.assertTrue;

import com.mzx.gulimall.threeserver.GuliMallThreeServerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Unit test for simple App.
 */
@SpringBootTest(classes = GuliMallThreeServerApplication.class)
@RunWith(SpringRunner.class)
public class AppTest {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void t1(){


        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set("xxx","xxx");

        System.out.println(1);
    }


}
