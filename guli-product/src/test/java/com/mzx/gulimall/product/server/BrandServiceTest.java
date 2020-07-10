package com.mzx.gulimall.product.server;

import com.mzx.gulimall.product.SpringApplicationProductApp;
import com.mzx.gulimall.product.entity.BrandEntity;
import com.mzx.gulimall.product.service.BrandService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author ZhenXinMa
 * @date 2020/7/10 16:38
 */
@SpringBootTest(classes = {SpringApplicationProductApp.class})
@RunWith(SpringRunner.class)
public class BrandServiceTest {


    @Resource
    private BrandService brandService;

    @Test
    public void t1() {

        BrandEntity entity = brandService.getById(1);
        System.out.println(entity);

    }


}
