package com.mzx.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.product.entity.SpuInfoDescEntity;
import com.mzx.gulimall.product.vo.SpuSaveVo;

import java.util.Map;

/**
 * spu
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 12:26:27
 */
public interface SpuInfoDescService extends IService<SpuInfoDescEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 实现将SPU的的描述图片集进行保存.
     *
     * @param vo
     * @param spuId SPU的ID.
     * @return
     */
    void saveSpuDesc(Long spuId, SpuSaveVo vo);
}

