package com.mzx.gulimall.cart;

import com.mzx.gulimall.cart.feign.ProductServiceFeign;
import com.mzx.gulimall.common.utils.R;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import sun.misc.Unsafe;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZhenXinMa
 * @date 2020/9/6 16:26
 */
@SpringBootTest(classes = {GuliMallCartApplication.class})
@RunWith(SpringRunner.class)
public class FeignTest {

    @Autowired
    private ProductServiceFeign productServiceFeign;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void t1() throws IOException {


        R info = productServiceFeign.info(14L);

        Map<String, Object> o = (Map<String, Object>) info.get("skuInfo");
        String defaultImg = (String) o.get("skuDefaultImg");
        String skuName = (String) o.get("skuName");
        String skuTitle = (String) o.get("skuTitle");
        // double不能转换成String.
        Double price = (Double) o.get("price");
        System.out.println(1);

    }

    @Test
    public void t2() {

        R attr = productServiceFeign.findSkuSaleAttr(14L);
        System.out.println(1);

        // 这样可以直接获取到size大小.
        Object size = attr.get("size");
        if ("1".equals(size)) {

            System.out.println("111");
            Object o = attr.get("attr");
            System.out.println(o);
            System.out.println(1);

        } else {

            System.out.println("222");

        }


    }

    @Test
    public void t3() {

        // 测试是直接删除还是直接覆盖.
        HashOperations<String, Object, Object> ops = stringRedisTemplate.opsForHash();
        HashMap<String, String> map = new HashMap<>();
        map.put("x", "1110000");
        ops.putAll("cart:50", map);

    }

    @Test
    public void t4() {

        BoundHashOperations<String, Object, Object> ops = stringRedisTemplate.boundHashOps("cart:55");
        Object x = ops.get("x1");
        System.out.println(x.toString());
        System.out.println(1);

    }

    @Test
    public void t5() {

        String key = "cart:c3659490-179d-46f1-a316-45b07218a08b";
        List<Object> values = stringRedisTemplate.opsForHash().values(key);
        System.out.println(values);
        System.out.println(1);

    }

    @Test
    public void t6() {

        // 测试B
        BigDecimal bigDecimal = new BigDecimal(0);
        for (int i = 0; i < 5; i++) {

            BigDecimal b = new BigDecimal(i);
            bigDecimal = bigDecimal.add(b);
        }

        System.out.println(bigDecimal);


    }


}
