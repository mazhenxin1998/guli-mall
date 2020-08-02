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

import java.util.List;
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

    @Override
    public boolean getStock(Long skuId) {

        QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("sku_id", skuId);
        List<WareSkuEntity> entities = baseMapper.selectList(wrapper);
        // 考虑到由于不同仓库存有相同的货物，所以说根据SKU查询的结果为一个集合
        // 现在要做的就是但凡一个仓库有存货就返回true.
        boolean flag = false;
        // TODO: 按道理来说，这里是不需要这个flag的.
        for (int i = 0; i < entities.size(); i++) {

            if (entities.get(i).getStock() > 0) {

                // 这里不需要再添加break了,直接返回就行.
                flag = true;
                return true;
            }

        }

        return flag;
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