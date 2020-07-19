package com.mzx.gulimall.product.dao;

import com.mzx.gulimall.product.entity.CategoryBrandRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Ʒ
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 12:26:27
 */
@Mapper
public interface CategoryBrandRelationDao extends BaseMapper<CategoryBrandRelationEntity> {

    /**
     * 通过分类的ID来修改所有引用了该分类ID的所有名字.
     *
     * @param entity
     */
    void updateByCategoryId(@Param(value = "entity") CategoryBrandRelationEntity entity);

    /**
     * 通过品牌的ID对品牌的名字进行全部修改.
     *
     * @param entity
     */
    void updateBrandNameByBrandId(@Param(value = "entity") CategoryBrandRelationEntity entity);
}
