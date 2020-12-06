package com.mzx.gulimall.seckill.vo;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-12-01 18:46 周二.
 */
@Data
@ToString
public class SkuInfoVo {

    private Long skuId;
    /**
     * spuId
     */
    private Long spuId;
    /**
     * sku
     */
    private String skuName;
    /**
     * sku
     */
    private String skuDesc;
    /**
     *
     */
    private Long catalogId;
    /**
     * Ʒ
     */
    private Long brandId;
    /**
     * Ĭ
     */
    private String skuDefaultImg;
    /**
     *
     */
    private String skuTitle;
    /**
     *
     */
    private String skuSubtitle;
    /**
     *
     */
    private BigDecimal price;
    /**
     *
     */
    private Long saleCount;

}
