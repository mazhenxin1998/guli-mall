package com.mzx.gulimall.product.dao;

import com.mzx.gulimall.product.SpringApplicationProductApp;
import com.mzx.gulimall.product.vo.web.SkuItemSaleAttrVo;
import com.mzx.gulimall.product.vo.web.SkuItemVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author ZhenXinMa
 * @date 2020/8/19 18:22
 */
@SpringBootTest(classes = {SpringApplicationProductApp.class})
@RunWith(SpringRunner.class)
public class SkuSaleAttrValueDaoTest {


    @Autowired
    private SkuSaleAttrValueDao skuSaleAttrValueDao;

    @Test
    public void t1(){

        List<SkuItemSaleAttrVo> saleAttrs = skuSaleAttrValueDao.getSaleAttrs(13L);
        saleAttrs.forEach(item->{

            System.out.println(item.toString());
        });
        System.out.println(1);

    }

    @Test
    public void t2(){

        List<String> skuSaleAttr = skuSaleAttrValueDao.getSkuSaleAttr(14L);
        System.out.println(1);
    }


}
