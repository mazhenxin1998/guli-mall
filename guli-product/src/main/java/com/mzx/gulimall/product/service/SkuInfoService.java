package com.mzx.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.product.entity.SkuInfoEntity;
import com.mzx.gulimall.product.vo.SpuSaveVo;

import java.util.Map;

/**
 * sku
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 12:26:27
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 从SpuSaveVo中抽离出来SkuBaseInfo的信息进行保存.
     *
     * @param spuId
     * @param vo
     */
    void saveSkuBaseInfo(Long spuId, SpuSaveVo vo);

    /**
     * 当查询参数里面的查询匹配的值是空的时候查询所有，如果不是空则根据查询条件查询.
     *
     * @param params
     * @return
     */
    PageUtils queryPageDetails(Map<String, Object> params);
}

