package com.mzx.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.Query;
import com.mzx.gulimall.ware.dao.WareSkuDao;
import com.mzx.gulimall.ware.entity.WareSkuEntity;
import com.mzx.gulimall.ware.service.WareSkuService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                new QueryWrapper<WareSkuEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageDetails(Map<String, Object> params) {

        QueryWrapper<WareSkuEntity> wrapper = this.getQueryWrapper(params);

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                wrapper
        );

        Integer count = baseMapper.selectCount(wrapper);
        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setTotalCount(count);

        return pageUtils;
    }

    private QueryWrapper<WareSkuEntity> getQueryWrapper(Map<String, Object> params) {

        QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<>();
        // skuId
        Object skuId = params.get("skuId");
        if (skuId != null) {

            if (!StringUtils.isEmpty(skuId)) {

                long skuid = Long.parseLong(skuId.toString());
                wrapper.and(wr -> {

                    wr.eq("sku_id", skuid);
                });

            }

        }

        Object wareId = params.get("wareId");
        if (wareId != null) {

            if (!StringUtils.isEmpty(wareId)) {

                long wareid = Long.parseLong(wareId.toString());
                wrapper.and(wr -> {

                    wr.eq("ware_id", wareid);
                });

            }

        }

        return wrapper;
    }


}