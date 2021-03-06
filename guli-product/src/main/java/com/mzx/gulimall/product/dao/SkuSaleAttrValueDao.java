package com.mzx.gulimall.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzx.gulimall.product.entity.SkuSaleAttrValueEntity;
import com.mzx.gulimall.product.vo.web.SkuItemSaleAttrVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sku
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 12:26:27
 */
@Mapper
public interface SkuSaleAttrValueDao extends BaseMapper<SkuSaleAttrValueEntity> {
    /**
     * 根据SPU的ID查询出该SPU下所有关联的SKU的销售的属性.
     *
     * @param spuId
     * @return
     */
    List<SkuItemSaleAttrVo> getSaleAttrs(@Param(value = "spuId") Long spuId);

    /**
     * 根据SKU的ID查询出该SKU的销售属性.
     *
     * @param skuId
     * @return
     */
    List<String> getSkuSaleAttr(@Param(value = "skuId") Long skuId);

}
