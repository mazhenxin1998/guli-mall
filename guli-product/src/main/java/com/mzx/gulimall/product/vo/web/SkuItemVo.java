package com.mzx.gulimall.product.vo.web;

import com.mzx.gulimall.product.entity.SkuImagesEntity;
import com.mzx.gulimall.product.entity.SkuInfoEntity;
import com.mzx.gulimall.product.entity.SpuInfoDescEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author ZhenXinMa
 * @date 2020/8/19 16:37
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SkuItemVo {
    /**
     * 不去查询该字段表示当前商品有存货.
     */
    private boolean hashStock = true;

    /**
     * SKU的基本信息: 名称、价格等 pms_sku_Info
     */
    private SkuInfoEntity info;
    /**
     * SKU的图片集.
     */
    private List<SkuImagesEntity> images;

    /**
     * SPU销售属性的集合.
     */
    private List<SkuItemSaleAttrVo> saleAttr;

    /**
     * 获取SPU的介绍.
     */
    private SpuInfoDescEntity desp;

    /**
     * 获取SPU规格参数信息. 规格参数就不展示了. 因为再录入的时候没有录入进去.
     */
    private List<SkuItemAttrGroupVo> groupAttrs;

}
