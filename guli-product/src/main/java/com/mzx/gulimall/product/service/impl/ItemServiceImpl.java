package com.mzx.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.mzx.gulimall.product.constant.RedisConstant;
import com.mzx.gulimall.product.entity.SkuImagesEntity;
import com.mzx.gulimall.product.entity.SkuInfoEntity;
import com.mzx.gulimall.product.service.ItemService;
import com.mzx.gulimall.product.service.SkuImagesService;
import com.mzx.gulimall.product.service.SkuInfoService;
import com.mzx.gulimall.product.service.SkuSaleAttrValueService;
import com.mzx.gulimall.product.vo.web.SkuItemSaleAttrVo;
import com.mzx.gulimall.product.vo.web.SkuItemVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ZhenXinMa
 * @date 2020/8/19 16:52
 */
@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    @Qualifier(value = "threadPoolExecutor")
    private ThreadPoolExecutor executor;

    public SkuItemVo itemFromDb(Long skuId) {

        /*
         * --------------------------------------------------------
         * 那么问题来了是先添加读锁还是先判断有没有数据.
         * 先添加读锁.
         * --------------------------------------------------------
         * */

        if (skuId != null && skuId > 0) {

            RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("GULI-RW-LOCK");
            // 获取读写锁中的读锁.
            RLock readLock = readWriteLock.readLock();
            // 使用默认的看门狗原理即可. 但是线上还是需要自定义事件的. 省去了续期操作.
            readLock.lock();
            try {

                // 每次获取读写锁之后先从缓存中读取该skuId所对应的数据,如果存在就直接返回, 如果不存在就从数据库中查询.


            } catch (Exception e) {

                e.printStackTrace();
            } finally {

                readLock.unlock();
            }
            SkuItemVo itemVo = new SkuItemVo();
            // 第一步设置 skuInfo
            SkuInfoEntity infoEntity = skuInfoService.getById(skuId);
            itemVo.setInfo(infoEntity);
            Long spuId = infoEntity.getSpuId();
            //  三级分类ID. 
            Long catalogId = infoEntity.getCatalogId();

            // 第二步设置 sku的图片集
            List<SkuImagesEntity> imagesEntities = skuImagesService.getSkuImages(skuId);
            itemVo.setImages(imagesEntities);

            // 第三步设置 spu销售属性集合 是根据SPU来获取.
            // TODO:  学习该SQL.等以后学了SQL回头来看看该SQL.
            // 获取销售属性: 根据SPUid.
            List<SkuItemSaleAttrVo> saleAttrVos = skuSaleAttrValueService.getAttrs(spuId);
            itemVo.setSaleAttr(saleAttrVos);
            return itemVo;
        } else {

            return null;
        }

    }

    @Override
    public SkuItemVo item(Long skuId) {

        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        if (skuId != null && skuId > 0) {

            RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(RedisConstant.LOCK_READ_WRITE);
            // 获取读写锁中的读锁.
            RLock readLock = readWriteLock.readLock();
            // 使用默认的看门狗原理即可. 但是线上还是需要自定义事件的. 省去了续期操作.
            readLock.lock();
            try {

                // 每次获取读写锁之后先从缓存中读取该skuId所对应的数据,如果存在就直接返回, 如果不存在就从数据库中查询.
                // 缓存商品的key : GULI:PRODUCT:skuId
                // 存的时候应该存JSON.
                String productCache = ops.get(RedisConstant.PRODUCT_CACHE_PREFIX + skuId.toString());
                if (!StringUtils.isEmpty(productCache)) {

                    // 不等于空则说明该商品在缓存中存了值, 直接返回即可。
                    // 我们存的时候是以JSON存的,那么取出来的时候我们应该以JSON取出来.
                    SkuItemVo skuItemVo = JSON.parseObject(productCache, SkuItemVo.class);
                    return skuItemVo;
                }

                // 这里我们从数据库中查询.
                // 但是从数据库中查询我们还需要注意的是缓存击穿问题。
                // 所以说我们再从数据库中读取数据的时候应该仅仅只允许一个用户进行读取,其读取了之后在存入缓存中.
                // TODO: 考虑一个问题: 是在下面的方法中将所读取的数据放入缓存中还是在该方法中将下面的数据放入缓存?
                // 应该是在下面的分布式锁之内将缓存放入缓存中. 否则会让多个用户去访问数据库.
                SkuItemVo skuItemVoFromDB = this.getSkuItemVoFromDB(skuId);
                return skuItemVoFromDB;

            } catch (Exception e) {

                // 出现异常也返回null。
                log.error("在查询商品信息的时候出现了异常: {}", e.getMessage());
                return null;

            } finally {

                // 虽然说最终finally会执行,但是只要这里没有return那么就执行上面的return.
                readLock.unlock();

            }

        } else {

            return null;

        }

    }

    private SkuItemVo getSkuItemVoFromDB(Long skuId) {

        System.out.println("缓存未命中,从DB中查询数据 方法getSkuItemVoFromDB执行了。。。");
        // skuID不需要进行判断 上一步已经判断了出来.
        // TODO: 在这里我们需要解决的是缓存击穿问题 同一时刻大量用户来访问DB.
        // 我们对其添加分布式锁,本地锁仅仅能锁住当前进程锁不住其他进程.
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        // 直接通过getLock获取的锁是可重入锁.
        RLock lock = redissonClient.getLock(RedisConstant.LOCK_REENTRANT);
        // 使用默认的事件以及续期.
        lock.lock();
        try {

            // 只要是获取到了锁的线程(分布式下)则执行否则不执行.
            // 先进来的第一件事情就是判断缓存里面有没有要查询的数据.
            // TODO: 这里需要对其添加读写锁吗？
            String s = ops.get(RedisConstant.PRODUCT_CACHE_PREFIX + skuId.toString());
            // 和上面一样.
            if(StringUtils.isNotEmpty(s)){

                // 直接返回.
                SkuItemVo skuItemVo = JSON.parseObject(s, SkuItemVo.class);
                return skuItemVo;

            }

            // 如果缓存里面没有则从DB中查询.
            SkuItemVo skuItemVo = this.Db(skuId);
            // 将其放到缓存里面.
            // 设置过期时间为三天.
            ops.set(RedisConstant.PRODUCT_CACHE_PREFIX + skuId.toString(),JSON.toJSONString(skuItemVo),
                    3, TimeUnit.DAYS);
            return skuItemVo;

        } catch (Exception e) {

            log.error("getSkuItemVoFromDB方法内发生了异常: {}",e.getMessage());
            return null;

        } finally {

            // 释放锁.
            lock.unlock();

        }

    }

    private SkuItemVo Db(Long skuId){

        System.out.println("线程: " + Thread.currentThread().getId() + "查询了一次DB》 ");
        // 这里采用异步编排对其进行优化.
        SkuItemVo itemVo = new SkuItemVo();
        // 第一步设置 skuInfo
        CompletableFuture<Long> infoFuture = CompletableFuture.supplyAsync(() -> {

            // 有返回值的异步.
            SkuInfoEntity infoEntity = skuInfoService.getById(skuId);
            itemVo.setInfo(infoEntity);
            return infoEntity.getSpuId();

        }, executor);

        // 第二步设置 sku的图片集  该异步不需要第一步的执行结果.
        CompletableFuture<Void> imgFuture = CompletableFuture.runAsync(() -> {

            List<SkuImagesEntity> imagesEntities = skuImagesService.getSkuImages(skuId);
            itemVo.setImages(imagesEntities);

        }, executor);

        // 第三步设置 spu销售属性集合 是根据SPU来获取. 该异步需要在infoFuture的执行结果才能执行.
        // TODO:  学习该SQL.等以后学了SQL回头来看看该SQL.
        // 获取销售属性: 根据SPUid.
        CompletableFuture<Void> saleAttrFuture = infoFuture.thenAcceptAsync((data) -> {

            // 这里的data就是上一步返回的SPU的ID.
            List<SkuItemSaleAttrVo> saleAttrVos = skuSaleAttrValueService.getAttrs(data);
            itemVo.setSaleAttr(saleAttrVos);

        }, executor);

        // return需要在      是哪个都执行完毕只有才能return.
        try {

            // get操作是异步的操作, 在所有异步没有执行完毕之前这里是阻塞的.
            CompletableFuture.allOf(infoFuture,imgFuture,saleAttrFuture).get();

        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();

        }

        // 当所有的异步任务都结束之后在接着往下执行代码.
        return itemVo;

    }

}
