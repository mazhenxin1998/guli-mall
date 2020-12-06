package com.mzx.gulimall.product.dao;

import com.mzx.gulimall.product.SpringApplicationProductApp;
import com.mzx.gulimall.product.entity.SkuInfoEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-12-05 17:16 周六.
 */
@SpringBootTest(classes = SpringApplicationProductApp.class)
@RunWith(SpringRunner.class)
public class SkuInfoDaoTest {

    @Autowired
    private SkuInfoDao skuInfoDao;

    @Test
    public void t1(){

        SkuInfoEntity entity = skuInfoDao.getById(14l);
        System.out.println(entity.toString());

    }

}