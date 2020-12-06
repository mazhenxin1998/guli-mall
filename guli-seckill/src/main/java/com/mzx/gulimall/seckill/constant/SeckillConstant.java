package com.mzx.gulimall.seckill.constant;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-12-01 18:58 周二.
 */
public class SeckillConstant {

    /**
     * 保存活动会话的前缀.
     */
    public static final String SECKILL_PREFIX = "SECKILL:SESSIONS:";
    /**
     * 缓存要上架的商品在Redis中.
     */
    public static final String SECKILL_PRODUCT_CACHE_PREFIX = "SECKILL:SKUS:";

    /**
     * 以SKU库存作为分布式信号量的基准进行限流.
     * <p>
     * 后面添加的是商品的随机码.
     */
    public static final String SECKILL_STOCK_SKU = "SECKILL:STOCK:SKU:";

    public static final String SECKILL_PUBLISH_IDEMPOTENT = "SECKILL:IDEMPOTENT";


}
