package com.mzx.gulimall.order.feign;

import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.order.GuliMallOrderApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-28 20:57 周一.
 */
@SpringBootTest(classes = GuliMallOrderApplication.class)
@RunWith(SpringRunner.class)
public class WaeServiceFeignTest {

    @Autowired
    private WareServiceFeign wareServiceFeign;

    @Test
    public void t1() {

        // TODO 这里是有问题的 .
        String[] ids = new String[]{"14", "15", "16"};
        R r = wareServiceFeign.getListStock(ids);
        Object data = r.get("data");
        System.out.println(1);

    }


}


