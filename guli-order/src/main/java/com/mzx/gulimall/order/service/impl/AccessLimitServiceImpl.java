package com.mzx.gulimall.order.service.impl;

import com.mzx.gulimall.order.service.IAccessLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-25 00:14 周五.
 */
@Service
public class AccessLimitServiceImpl implements IAccessLimitService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public void setKeyWithExpiration(String key, int maxCount, int seconds) {

        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set(key, String.valueOf(maxCount), seconds, TimeUnit.SECONDS);

    }

    @Override
    public String getValue(String key) {

        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        // 这个值可能是空值.
        return ops.get(key);

    }

    @Override
    public void keyIncrement(String key) {

        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        // 实现自增操作.
        ops.increment(key);

    }
}
