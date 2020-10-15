package com.mzx.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.Query;
import com.mzx.gulimall.ware.dao.WareOrderTaskDao;
import com.mzx.gulimall.ware.entity.WareOrderTaskEntity;
import com.mzx.gulimall.ware.service.WareOrderTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;


@Service("wareOrderTaskService")
public class WareOrderTaskServiceImpl extends ServiceImpl<WareOrderTaskDao, WareOrderTaskEntity> implements WareOrderTaskService {

    @Autowired
    private WareOrderTaskDao wareOrderTaskDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareOrderTaskEntity> page = this.page(
                new Query<WareOrderTaskEntity>().getPage(params),
                new QueryWrapper<WareOrderTaskEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageDetails(Map<String, Object> params) {


        QueryWrapper<WareOrderTaskEntity> wrapper = this.getQueryWrapper(params);
        IPage<WareOrderTaskEntity> page = this.page(
                new Query<WareOrderTaskEntity>().getPage(params),
                wrapper
        );

        Integer count = baseMapper.selectCount(wrapper);
        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setTotalCount(count);

        return pageUtils;
    }

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public WareOrderTaskEntity getOrderTaskByOrderSn(String orderSn) {

        return wareOrderTaskDao.getOrderTaskByOrderSn(orderSn);

    }

    private QueryWrapper<WareOrderTaskEntity> getQueryWrapper(Map<String, Object> params) {

        QueryWrapper<WareOrderTaskEntity> wrapper = new QueryWrapper<>();
        Object key = params.get("key");
        if (key != null) {

            if (!StringUtils.isEmpty(key)) {

                long id = Long.parseLong(key.toString());
                wrapper.and(w -> {

                    w.eq("id", id).or().eq("order_id", id);
                });
            }

        }

        return wrapper;
    }

}