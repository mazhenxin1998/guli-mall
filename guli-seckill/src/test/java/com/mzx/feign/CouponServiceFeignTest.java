package com.mzx.feign;

import com.mzx.gulimall.seckill.GuliMallSecKillApplication;
import com.mzx.gulimall.seckill.feign.CouponServiceFeign;
import com.mzx.gulimall.seckill.vo.ResultVo;
import com.mzx.gulimall.seckill.vo.SeckillSessionEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-12-01 16:10 周二.
 */
@SpringBootTest(classes = GuliMallSecKillApplication.class)
@RunWith(SpringRunner.class)
public class CouponServiceFeignTest {

    @Resource
    private CouponServiceFeign couponServiceFeign;

    @Test
    public void t1() {

        ResultVo<SeckillSessionEntity> resultVo = couponServiceFeign.getLatesSession();
        System.out.println(resultVo.toString());

    }

    @Test
    public void t2() {

        ResultVo<SeckillSessionEntity> vo = couponServiceFeign.getLatesSession();
        System.out.println(vo.toString());

    }


}
