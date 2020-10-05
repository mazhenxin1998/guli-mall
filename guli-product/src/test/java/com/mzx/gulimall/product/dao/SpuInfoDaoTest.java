package com.mzx.gulimall.product.dao;

import com.mzx.gulimall.product.SpringApplicationProductApp;
import com.mzx.gulimall.product.entity.SpuInfoEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-04 21:40 周日.
 */
@SpringBootTest(classes = SpringApplicationProductApp.class)
@RunWith(SpringRunner.class)
public class SpuInfoDaoTest {

    @Autowired
    private SpuInfoDao spuInfoDao;

    @Test
    public void t1() {

        // OK.
        SpuInfoEntity infoEntity = spuInfoDao.getSpuInfoBySkuId(15L);
        System.out.println(1);

    }

}
