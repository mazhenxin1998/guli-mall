package com.mzx.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.product.entity.SpuInfoEntity;
import com.mzx.gulimall.product.vo.SpuSaveVo;

import java.util.Map;

/**
 * spu
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 12:26:27
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 商品SPU的详细增加功能.
     * @param vo
     * @return
     */
    R saveSpuDetails(SpuSaveVo vo);
}

