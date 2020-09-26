package com.mzx.gulimall.cart;

import com.mzx.gulimall.cart.service.GuliWebCartService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-26 17:55 周六.
 */
@SpringBootTest(classes = GuliMallCartApplication.class)
@RunWith(SpringRunner.class)
public class RedisTest {

    @Autowired
    private GuliWebCartService guliWebCartService;

    @Test
    public void t1() {

        BoundHashOperations<String, Object, Object> boundHash = guliWebCartService
                .getBoundHash("9ec3efd3-5a0f-4b43-a7db-17d2b1eaa32b");
        // 测试删除.
        Long delete = boundHash.delete("14");
        // 返回1应该是操作成功.
        System.out.println(delete);

    }

    @Test
    public void t2(){

    }


}
