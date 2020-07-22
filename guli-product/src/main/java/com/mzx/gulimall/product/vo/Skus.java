/**
 * Copyright 2019 bejson.com
 */
package com.mzx.gulimall.product.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 通过笛卡尔积获取到的每一个SKU.
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class Skus {

    /**
     * 每一个Sku的基本属性，而每一个SKU可能有多个Attr所以用List来包装.
     */
    private List<Attr> attr;
    /**
     * SKU的基本信息.
     */
    private String skuName;
    /**
     * 使用BigDecimal进行计算会确保精度不丢失.
     */
    private BigDecimal price;
    private String skuTitle;
    /**
     * 副标题.
     */
    private String skuSubtitle;


    private List<Images> images;
    /**
     * 以逗号隔开将其存进去.
     */
    private List<String> descar;
    /**
     * 满足fullCount的件数则打discount的折扣.
     */
    private int fullCount;
    private BigDecimal discount;
    /**
     * 应该是 是否可叠加优惠.
     */
    private int countStatus;
    /**
     * 满fullPrice的价格后减少reducePrice的价格.
     */
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    /**
     * 应该是是否可叠加优惠.
     */
    private int priceStatus;

    /**
     * 每一个Sku对应不同的会员价格.
     */
    private List<MemberPrice> memberPrice;


}