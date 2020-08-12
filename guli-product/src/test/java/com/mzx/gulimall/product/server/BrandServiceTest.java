package com.mzx.gulimall.product.server;

import com.mzx.gulimall.product.SpringApplicationProductApp;
import com.mzx.gulimall.product.entity.BrandEntity;
import com.mzx.gulimall.product.service.BrandService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author ZhenXinMa
 * @date 2020/7/10 16:38
 */
@SpringBootTest(classes = {SpringApplicationProductApp.class})
@RunWith(SpringRunner.class)
public class BrandServiceTest {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private BrandService brandService;

    @Test
    public void t1() {

        BrandEntity entity = brandService.getById(1);
        System.out.println(entity);

    }

    @Test
    public void t2() {

        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set("test", "111");
        System.out.println(ops.get("test"));


    }

    @Test
    public void t3() {

        Double random = Math.random() * 10 + 30;
        long l = random.longValue();

        System.out.println("Time: " + l);
        System.out.println("1");

    }


}
