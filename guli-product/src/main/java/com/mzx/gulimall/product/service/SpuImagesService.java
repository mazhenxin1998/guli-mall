package com.mzx.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.product.entity.SpuImagesEntity;
import com.mzx.gulimall.product.vo.SpuSaveVo;

import java.util.Map;

/**
 * spuͼƬ
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 12:26:27
 */
public interface SpuImagesService extends IService<SpuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存SPU的图片信息，该图片以SPU的名字来进行命名.
     * @param spuId
     * @param vo
     */
    void saveSpuImages(Long spuId, SpuSaveVo vo);
}

