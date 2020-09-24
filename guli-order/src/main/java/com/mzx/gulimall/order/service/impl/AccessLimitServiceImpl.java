package com.mzx.gulimall.order.service.impl;

import com.mzx.gulimall.order.service.IAccessLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-25 00:14 周五.
 */
@Service
public class AccessLimitServiceImpl implements IAccessLimitService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;




}
