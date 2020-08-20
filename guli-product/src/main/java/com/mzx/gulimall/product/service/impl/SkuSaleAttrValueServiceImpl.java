package com.mzx.gulimall.product.service.impl;

import com.mzx.gulimall.product.vo.web.SkuItemSaleAttrVo;
import com.mzx.gulimall.product.vo.web.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.Query;

import com.mzx.gulimall.product.dao.SkuSaleAttrValueDao;
import com.mzx.gulimall.product.entity.SkuSaleAttrValueEntity;
import com.mzx.gulimall.product.service.SkuSaleAttrValueService;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Autowired
    private SkuSaleAttrValueDao skuSaleAttrValueDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuItemSaleAttrVo> getAttrs(Long spuId) {

        // spuID不需要进行校验, 因为传进来的值一定是在数据库中存在的.
        List<SkuItemSaleAttrVo> attrs = skuSaleAttrValueDao.getSaleAttrs(spuId);
        return attrs;
    }

}