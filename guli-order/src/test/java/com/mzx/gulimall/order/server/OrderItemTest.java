package com.mzx.gulimall.order.server;

import com.mzx.gulimall.order.entity.OrderItemEntity;
import com.mzx.gulimall.order.service.OrderItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author ZhenXinMa
 * @date 2020/7/10 17:05
 */
@SpringBootTest(classes = {SpringApplicationOrderApp.class})
@RunWith(SpringRunner.class)
public class OrderItemTest {


    @Resource
    private OrderItemService orderItemService;

    @Test
    public void t1() {

        OrderItemEntity entity = orderItemService.getById(1);
        System.out.println(entity);

    }


}
