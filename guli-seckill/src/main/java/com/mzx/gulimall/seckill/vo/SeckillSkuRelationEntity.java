package com.mzx.gulimall.seckill.vo;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-12-01 18:20 周二.
 */
@Data
@ToString
public class SeckillSkuRelationEntity {

    /**
     * id
     */
    private Long id;
    /**
     * 该ID表示的是SessionID.
     */
    private Long promotionId;
    /**
     * 表示的也是当场秒杀会话的id.
     */
    private Long promotionSessionId;
    /**
     * 关联商品.
     */
    private Long skuId;
    /**
     * 秒杀价格.
     */
    private BigDecimal seckillPrice;
    /**
     * 预定秒杀的数量.
     */
    private Integer seckillCount;
    /**
     * 秒杀限制？
     */
    private Integer seckillLimit;
    /**
     * 秒杀的排序.
     */
    private Integer seckillSort;

    /**
     * 当前商品的描述信息.
     */
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
