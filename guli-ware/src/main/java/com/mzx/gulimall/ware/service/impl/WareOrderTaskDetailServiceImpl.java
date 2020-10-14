package com.mzx.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.Query;
import com.mzx.gulimall.ware.dao.WareOrderTaskDetailDao;
import com.mzx.gulimall.ware.entity.WareOrderTaskDetailEntity;
import com.mzx.gulimall.ware.service.WareOrderTaskDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("wareOrderTaskDetailService")
public class WareOrderTaskDetailServiceImpl extends ServiceImpl<WareOrderTaskDetailDao, WareOrderTaskDetailEntity> implements WareOrderTaskDetailService {

    @Autowired
    private WareOrderTaskDetailDao wareOrderTaskDetailDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareOrderTaskDetailEntity> page = this.page(
                new Query<WareOrderTaskDetailEntity>().getPage(params),
                new QueryWrapper<WareOrderTaskDetailEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<WareOrderTaskDetailEntity> getOrderTaskDetailsByStockId(Long stockId) {

        //  可能会出现空.
        if (stockId == null || stockId <= 0) {

            return wareOrderTaskDetailDao.getOrderTaskDetailsByStockId(stockId);

        }

        return null;
    }

}