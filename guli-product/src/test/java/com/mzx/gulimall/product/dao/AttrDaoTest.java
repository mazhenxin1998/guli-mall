package com.mzx.gulimall.product.dao;

import com.mzx.gulimall.product.SpringApplicationProductApp;
import com.mzx.gulimall.product.vo.Attr;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author ZhenXinMa
 * @date 2020/8/12 20:05
 */
@SpringBootTest(classes = {SpringApplicationProductApp.class})
@RunWith(SpringRunner.class)
public class AttrDaoTest {

    @Autowired
    private AttrDao attrDao;

    @Test
    public void t1() {

        List<Attr> attrList = attrDao.findAttrsByCatalogId(225L);
        for (Attr attr : attrList) {

            System.out.println(attr.toString());
        }

        System.out.println(1);

    }


}
