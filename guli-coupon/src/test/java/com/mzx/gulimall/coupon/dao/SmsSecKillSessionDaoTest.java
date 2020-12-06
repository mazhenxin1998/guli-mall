package com.mzx.gulimall.coupon.dao;

import com.mzx.gulimall.coupon.SpringApplicationCouponApp;
import com.mzx.gulimall.coupon.entity.SmsSeckillSessionEntity;
import com.mzx.gulimall.coupon.service.SmsSeckillSessionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-26 15:27 周一.
 */
@SpringBootTest(classes = SpringApplicationCouponApp.class)
@RunWith(SpringRunner.class)
public class SmsSecKillSessionDaoTest {

    @Autowired
    private SmsSeckillSessionService smsSeckillSessionService;

    @Resource
    private SmsSeckillSessionDao smsSeckillSessionDao;

    @Test
    public void t1(){

        SmsSeckillSessionEntity entity = smsSeckillSessionService.getById("1");
        System.out.println(entity.toString());

    }

    @Test
    public void t2(){

        smsSeckillSessionService.getLatesSession();

    }


}
