package com.mzx.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.product.entity.CategoryBrandRelationEntity;
import com.mzx.gulimall.product.vo.CategoryBrandRelationVo;

import java.util.List;
import java.util.Map;

/**
 * Ʒ
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 12:26:27
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 品牌和分类进行关联.
     * <p>
     * 查询pms_category_brand_relation表根据品牌ID.
     *
     * @param params
     * @param brandId
     * @return
     */
    PageUtils queryPage(Map<String, Object> params, String brandId);

    /**
     * 保存品牌和分类联合的信息。
     * <p>
     * 通过CategoryBrandRelationEntity获取到分类和品牌的ID来获取到属于该ID的名字.
     * 然后进行全保存.
     *
     * @param categoryBrandRelation
     */
    void saveDetail(CategoryBrandRelationEntity categoryBrandRelation);

    /**
     * 查询当前分类下的所有品牌信息.
     *
     * @param catId
     * @return
     */
    List<CategoryBrandRelationVo> getBrandsByCategoryId(Long catId);
}

