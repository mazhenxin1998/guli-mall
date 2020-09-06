package com.mzx.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.product.entity.SkuSaleAttrValueEntity;
import com.mzx.gulimall.product.vo.web.SkuItemSaleAttrVo;

import java.util.List;
import java.util.Map;

/**
 * sku
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 12:26:27
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据SPU的ID查询出该SPU下所有关联的SKU的销售属性.
     *
     * @param spuId
     * @return
     */
    List<SkuItemSaleAttrVo> getAttrs(Long spuId);

    /**
     * 根据SKU的ID查询出销售属性.
     *
     * @param id
     * @return
     */
    R findSkuSaleAttr(Long id);
}

