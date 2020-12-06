package com.mzx.gulimall.seckill.service.impl;

import com.alibaba.fastjson.JSON;
import com.mzx.gulimall.seckill.constant.SeckillConstant;
import com.mzx.gulimall.seckill.feign.CouponServiceFeign;
import com.mzx.gulimall.seckill.feign.IProductServiceFeign;
import com.mzx.gulimall.seckill.service.ISecKillService;
import com.mzx.gulimall.seckill.vo.*;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-12-01 18:28 周二.
 */
@Service
public class SecKillServiceImpl implements ISecKillService {

    @Autowired
    private CouponServiceFeign couponServiceFeign;

    @Autowired
    private IProductServiceFeign iProductServiceFeign;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void putWay() {

        /*
         * 接口暂时没有做幂等接口.
         * */
        ResultVo<SeckillSessionEntity> resultVo = couponServiceFeign.getLatesSession();
        if (resultVo.getCode() == 0) {

            // 表示远程请求没有出现错误.
            List<SeckillSessionEntity> sessions = resultVo.getList();
            // 1. 保存活动信息.
            this.saveSessionInfo(sessions);
            // 2. 缓存商品的详细信息.
            this.saveSessionRelationsSkuInfo(sessions);
            // 3. 利用分布式信号量来进行限流. 也就是将库存设置为信号量的值.

        }

    }

    /**
     * 保存活动信息具体执行的方法.
     * <p>
     * 保存的Key是 活动开始时间-活到结束时间.
     * 保存的Value是该活动所关联的所有的商品.
     *
     * @param sessions
     */
    private void saveSessionInfo(List<SeckillSessionEntity> sessions) {

        // session存储结构为: seckll:sessions:start-end  value:[skuIds]
        sessions.stream().forEach(session -> {

            // 使用Redis存储的存储
            long start = session.getStartTime().getTime();
            long end = session.getEndTime().getTime();
            // 保存当前会话的key.
            String key = SeckillConstant.SECKILL_PREFIX + start + "_" + end;
            if (!stringRedisTemplate.hasKey(key)) {

                // 保存session之前需要看看其是否存在，然后在进行保存.
                List<String> collect =
                        session.getRelations().stream().map(item -> item.getId() + "-" + item.getSkuId().toString())
                                .collect(Collectors.toList());
                stringRedisTemplate.opsForList().leftPushAll(key, collect);

            }

        });

    }

    /**
     * 保存上加商品的详细信息.
     *
     * @param sessions
     */
    private void saveSessionRelationsSkuInfo(List<SeckillSessionEntity> sessions) {

        // 准备操作Hash的Ops.
        BoundHashOperations<String, Object, Object> hashOps = stringRedisTemplate
                .boundHashOps(SeckillConstant.SECKILL_PRODUCT_CACHE_PREFIX);
        sessions.stream().forEach(session -> {

            // 每一个会话下面都是有多个商品要上架的.
            List<SeckillSkuRelationEntity> relations = session.getRelations();
            relations.stream().forEach(sku -> {

                if (!hashOps.hasKey(session.getId() + "-" + sku.getSkuId().toString())) {

                    // 表示redis中不存在该缓存的详细信息.

                    String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                    // 缓存的是商品的详细信息.
                    SeckillRedisVo redisVo = new SeckillRedisVo();
                    ResultVo<SkuInfoEntity> resultVo = iProductServiceFeign.getSkuInfo(sku.getSkuId());
                    if (resultVo.getCode() == 0) {

                        SkuInfoEntity infoEntity = new SkuInfoEntity();
                        BeanUtils.copyProperties(resultVo.getData(), infoEntity);
                        redisVo.setSkuInfo(infoEntity);

                    }

                    // 保存sku的秒杀信息.
                    long start = session.getStartTime().getTime();
                    long end = session.getEndTime().getTime();
                    redisVo.setStartTime(start);
                    redisVo.setEndTime(end);
                    redisVo.setPromotionSessionId(session.getId());
                    BeanUtils.copyProperties(sku, redisVo);
                    // 保存商品随机码.
                    redisVo.setRandomCode(uuid);
                    // 保存以商品库存的分布式信号量.
                    RSemaphore skuStockSemaphore = redissonClient
                            .getSemaphore(SeckillConstant.SECKILL_STOCK_SKU + redisVo.getRandomCode());
                    // 保存商品库存分布式信号量的时候不能以商品id进行保存, 应该以randomCode为唯一标识来进行标识.
                    skuStockSemaphore.trySetPermits(redisVo.getSeckillCount());
                    // 最后保存商品相信信息的时候以.
                    hashOps.put(session.getId() + "-" + sku.getSkuId().toString(),
                            JSON.toJSONString(redisVo.toString()));

                }

            });

        });

    }

}
