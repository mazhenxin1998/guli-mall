package com.mzx.gulimall.product.redisson;

import com.mzx.gulimall.product.SpringApplicationProductApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ZhenXinMa
 * @date 2020/8/6 21:29
 */
@SpringBootTest(classes = {SpringApplicationProductApp.class})
@RunWith(SpringRunner.class)
public class RedissonTest {

    @Autowired
    private RedissonClient redissonClient;


    @Test
    public void t1() {

        // 可重入锁 对比Java锁: ReentrantLock
        // 分布式锁.
        RLock lock = redissonClient.getLock("anyLock");
        // 加锁的时候不指定过期时间则默认使用看门狗.
        lock.lock();
        try {

            System.out.println(lock.toString());
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            // 这里会释放锁》
            lock.unlock();
        }

        System.out.println("1");
    }



}
