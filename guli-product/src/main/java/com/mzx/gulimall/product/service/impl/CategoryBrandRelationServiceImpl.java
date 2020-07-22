package com.mzx.gulimall.product.service.impl;

import com.mzx.gulimall.product.dao.BrandDao;
import com.mzx.gulimall.product.dao.CategoryDao;
import com.mzx.gulimall.product.entity.BrandEntity;
import com.mzx.gulimall.product.entity.CategoryEntity;
import com.mzx.gulimall.product.vo.CategoryBrandRelationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.Query;

import com.mzx.gulimall.product.dao.CategoryBrandRelationDao;
import com.mzx.gulimall.product.entity.CategoryBrandRelationEntity;
import com.mzx.gulimall.product.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private BrandDao brandDao;

    @Autowired
    private CategoryBrandRelationDao categoryBrandRelationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, String brandId) {

        // 通过brandId查询出该品牌所关联的所有分类信息.
        return null;
    }

    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {

        // 查询全保存.
        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        BrandEntity brandEntity = brandDao.selectById(brandId);
        categoryBrandRelation.setCatelogName(categoryEntity.getName());
        categoryBrandRelation.setBrandName(brandEntity.getName());
        // CategoryBrandRelationEntity保存的就是全部的信息
        baseMapper.insert(categoryBrandRelation);
        System.out.println("分类和品牌关联信息保存成功");
    }

    @Override
    public List<CategoryBrandRelationVo> getBrandsByCategoryId(Long catId) {

        QueryWrapper<CategoryBrandRelationEntity> wrapper = new QueryWrapper<CategoryBrandRelationEntity>()
                .eq("catelog_id", catId);
        HashMap<String, Object> params = new HashMap<String, Object>();
        IPage<CategoryBrandRelationEntity> page = this.page(

                new Query<CategoryBrandRelationEntity>().getPage(params),
                wrapper
        );

        List<CategoryBrandRelationEntity> brandRelationEntityList = page.getRecords();
        // 需要从品牌和分类中间表进行查询,查询出品牌ID, 然后将该品牌的所有信息返回.
        // 一个分类下可以有多个品牌.

        List<CategoryBrandRelationVo> collect = brandRelationEntityList.stream().map(item -> {

            // 从品牌数据库中查询出该品牌的相信信息.
            BrandEntity entity = brandDao.selectById(item.getBrandId());
            CategoryBrandRelationVo relationVo = new CategoryBrandRelationVo(entity.getBrandId(), entity.getName());
            return relationVo;
        }).collect(Collectors.toList());

        return collect;
    }

}