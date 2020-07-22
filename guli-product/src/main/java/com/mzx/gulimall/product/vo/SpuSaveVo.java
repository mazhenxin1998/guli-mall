/**
 * Copyright 2019 bejson.com
 */
package com.mzx.gulimall.product.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 要保存的Vo商品SPU和SKU的信息.
 *
 * @author ZhenXinMa
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class SpuSaveVo {

    /**
     * 以下基本信息属于 SPU的基本信息.
     */
    private String spuName;
    private String spuDescription;
    private Long catalogId;
    private Long brandId;
    private BigDecimal weight;
    private int publishStatus;
    // ------------

    /**
     * 商品的描述信息，通过图片..
     */
    private List<String> decript;

    // -----------
    /**
     * 商品图片集.images不应该只是String类型啊
     */
    private List<String> images;

    /**
     * 优惠券相关.
     */
    private Bounds bounds;

    /**
     * 基础属性.
     */
    private List<BaseAttrs> baseAttrs;

    /**
     * 笛卡尔积得来的所有SKU。
     */
    private List<Skus> skus;


}