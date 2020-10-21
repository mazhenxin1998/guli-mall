package com.mzx.gulimall.feign;

import com.mzx.gulimall.common.order.Result;
import com.mzx.gulimall.pay.GuliMallPaymentApplication;
import com.mzx.gulimall.pay.feign.OrderServiceFeign;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-21 19:37 周三.
 */
@SpringBootTest(classes = GuliMallPaymentApplication.class)
@RunWith(SpringRunner.class)
public class OrderServiceFeignTest {

    @Autowired
    private OrderServiceFeign orderServiceFeign;

    @Test
    public void t1(){

        Result result = orderServiceFeign.getOrderByOrderSn("202010191509592061318086978515853314");
        System.out.println(1);

    }

}
