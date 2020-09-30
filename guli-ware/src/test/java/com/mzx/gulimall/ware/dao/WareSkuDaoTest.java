package com.mzx.gulimall.ware.dao;

import com.mzx.gulimall.ware.GuliMallWareApplication;
import com.mzx.gulimall.ware.vo.SkuWareStockTo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-28 17:17 周一.
 */
@SpringBootTest(classes = GuliMallWareApplication.class)
@RunWith(SpringRunner.class)
public class WareSkuDaoTest {

    @Autowired
    private WareSkuDao wareSkuDao;

    @Test
    public void t1(){

        List<Long> ids = new ArrayList<>();
        ids.add(14L);
        ids.add(15L);
        ids.add(16L);
        List<SkuWareStockTo> list = wareSkuDao.listFindStock(ids);
        System.out.println(1);

    }


}
