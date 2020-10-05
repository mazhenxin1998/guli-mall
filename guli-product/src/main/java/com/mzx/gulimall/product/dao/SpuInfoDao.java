package com.mzx.gulimall.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzx.gulimall.product.entity.SpuInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * spu
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 12:26:27
 */
@Mapper
@Repository
public interface SpuInfoDao extends BaseMapper<SpuInfoEntity> {
    /**
     * 根据SKU的ID查询SPU详细信息.
     *
     * @param skuId
     * @return
     */
    SpuInfoEntity getSpuInfoBySkuId(@Param(value = "skuId") Long skuId);
}
