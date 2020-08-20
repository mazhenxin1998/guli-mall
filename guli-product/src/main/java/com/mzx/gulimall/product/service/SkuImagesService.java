package com.mzx.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.product.entity.SkuImagesEntity;

import java.util.List;
import java.util.Map;

/**
 * skuͼƬ
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 12:26:27
 */
public interface SkuImagesService extends IService<SkuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据SKUid获取到该SKU所关联的所有图片.
     * @param skuId
     * @return
     */
    List<SkuImagesEntity> getSkuImages(Long skuId);
}

