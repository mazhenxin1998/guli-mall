package com.mzx.gulimall.seckill.vo;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 缓存商品详细信息.
 * <p>
 * 每个秒杀会话对应着多个秒杀详细信息.
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-12-01 19:14 周二.
 */
@Data
@ToString
public class SeckillRedisVo {


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
     * 每人限购数量.
     */
    private Integer seckillLimit;
    /**
     * 秒杀的排序.
     */
    private Integer seckillSort;

    /**
     * spuId
     */
    private Long spuId;

    /**
     * 保存的是当前秒杀商品关联的商品的详情.
     */
    private SkuInfoEntity skuInfo;

    private Long startTime;

    private Long endTime;

    /**
     * 商品随机码.
     */
    private String randomCode;


}
