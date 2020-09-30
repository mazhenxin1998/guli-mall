package com.mzx.gulimall.order.feign;

import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.order.GuliMallOrderApplication;
import com.mzx.gulimall.order.vo.SkuWareStockTo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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
        // 每一个List里面都是一个HashMap.
        List<LinkedHashMap<String, Object>> tos = (List<LinkedHashMap<String, Object>>) data;
        List<SkuWareStockTo> wares = new ArrayList<>();
        tos.forEach(item -> {

            Long stock = Long.valueOf(item.get("stock").toString());
            Long wareId = Long.valueOf(item.get("wareId").toString());
            Long skuId = Long.valueOf(item.get("skuId").toString());
            SkuWareStockTo ware = new SkuWareStockTo(stock, wareId, skuId);
            wares.add(ware);

        });
        System.out.println(1);

    }


}


