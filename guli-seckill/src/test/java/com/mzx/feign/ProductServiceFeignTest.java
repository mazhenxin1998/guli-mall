package com.mzx.feign;

import com.mzx.gulimall.seckill.GuliMallSecKillApplication;
import com.mzx.gulimall.seckill.feign.IProductServiceFeign;
import com.mzx.gulimall.seckill.vo.ResultVo;
import com.mzx.gulimall.seckill.vo.SkuInfoEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-12-05 17:23 周六.
 */
@SpringBootTest(classes = GuliMallSecKillApplication.class)
@RunWith(SpringRunner.class)
public class ProductServiceFeignTest {

    @Autowired
    private IProductServiceFeign iProductServiceFeign;

    @Test
    public void t1() {

        ResultVo<SkuInfoEntity> resultVo = iProductServiceFeign.getSkuInfo(14l);
        if (resultVo.getCode() == 0) {

            System.out.println(resultVo.toString());

        }

    }

}
