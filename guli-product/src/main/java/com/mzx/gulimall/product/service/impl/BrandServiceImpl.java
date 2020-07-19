package com.mzx.gulimall.product.service.impl;

import com.mzx.gulimall.product.dao.CategoryBrandRelationDao;
import com.mzx.gulimall.product.dao.CategoryDao;
import com.mzx.gulimall.product.entity.CategoryBrandRelationEntity;
import com.mzx.gulimall.product.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.Query;

import com.mzx.gulimall.product.dao.BrandDao;
import com.mzx.gulimall.product.entity.BrandEntity;
import com.mzx.gulimall.product.service.BrandService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


/**
 * @author lenovo
 */
@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {


    @Autowired
    private CategoryBrandRelationDao categoryBrandRelationDao;

    /**
     * 说明下这里使用线程共享的Dao组件而不用考虑非事务并发的缘由: 不管来几个线程, 其使用的Dao组件都不会修改任何一个共有的属性.
     * 所以说就不存在该条件下并发的情况.
     */
    @Autowired
    private CategoryDao categoryDao;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                new QueryWrapper<BrandEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateDetail(BrandEntity brand) {

        String name = brand.getName();
        if (!StringUtils.isEmpty(name)) {

            baseMapper.updateById(brand);
            CategoryBrandRelationEntity entity = new CategoryBrandRelationEntity();
            entity.setBrandId(brand.getBrandId());
            entity.setBrandName(name);
            categoryBrandRelationDao.updateBrandNameByBrandId(entity);
        } else {

            // 表示修改的不是name。
            baseMapper.updateById(brand);
        }

    }


}