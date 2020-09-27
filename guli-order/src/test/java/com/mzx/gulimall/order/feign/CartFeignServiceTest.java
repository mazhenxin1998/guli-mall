package com.mzx.gulimall.order.feign;

import com.mzx.gulimall.common.model.CartItem;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.order.GuliMallOrderApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-27 15:16 周日.
 */
@SpringBootTest(classes = {GuliMallOrderApplication.class})
@RunWith(SpringRunner.class)
public class CartFeignServiceTest {

    @Autowired
    private CartServiceFeign cartServiceFeign;

    @Test
    public void t1(){

        R checkedCartItems = cartServiceFeign.getCheckedCartItems();
        List<CartItem> items = (List<CartItem>) checkedCartItems.get("data");
        System.out.println(1);

    }


}
