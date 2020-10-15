package com.mzx.gulimall.ware.feign;

import com.mzx.gulimall.common.order.Result;
import com.mzx.gulimall.ware.GuliMallWareApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-15 00:34 周四.
 */
@SpringBootTest(classes = GuliMallWareApplication.class)
@RunWith(SpringRunner.class)
public class OrderServiceFeignTest {

    @Autowired
    private OrderServiceFeign orderServiceFeign;

    @Test
    public void t1(){

        String sn = "202010150026063531316414991141781505";
        orderServiceFeign.getOrderByOrderSn(sn);

    }

    @Test
    public void t2(){

        String sn = "202010152234274511316749281780150273";
        Result result = orderServiceFeign.getOrderByOrderSn(sn);

    }


}
