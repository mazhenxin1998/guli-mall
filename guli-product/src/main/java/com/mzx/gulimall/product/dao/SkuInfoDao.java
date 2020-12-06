package com.mzx.gulimall.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzx.gulimall.product.entity.SkuInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * sku
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 12:26:27
 */
@Mapper
@Repository
public interface SkuInfoDao extends BaseMapper<SkuInfoEntity> {


    /**
     * 根据SKUid查询其详细信息.
     *
     * @param id
     * @return
     */
    SkuInfoEntity getById(@Param("id") Long id);
}
