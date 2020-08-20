package com.mzx.gulimall.product.service.impl;

import com.mzx.gulimall.product.entity.SkuImagesEntity;
import com.mzx.gulimall.product.entity.SkuInfoEntity;
import com.mzx.gulimall.product.entity.SpuInfoDescEntity;
import com.mzx.gulimall.product.service.*;
import com.mzx.gulimall.product.vo.web.SkuItemAttrGroupVo;
import com.mzx.gulimall.product.vo.web.SkuItemSaleAttrVo;
import com.mzx.gulimall.product.vo.web.SkuItemVo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    @Override
    public SkuItemVo item(Long skuId) {

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
}
