package com.mzx.gulimall.product.dao;

import com.mzx.gulimall.product.entity.AttrGroupEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzx.gulimall.product.vo.web.SkuItemAttrGroupVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 12:26:27
 */
@Mapper
public interface AttrGroupDao extends BaseMapper<AttrGroupEntity> {
    /**
     * 通过分类ID和SPU的ID查询出该SPU下所有的分组以及分组下的所有属性.
     * @param spuId
     * @param catalogId
     * @return
     */
    List<SkuItemAttrGroupVo> getGroupAttrs(@Param(value = "spuId") Long spuId, @Param(value = "catalogId") Long catalogId);
}
