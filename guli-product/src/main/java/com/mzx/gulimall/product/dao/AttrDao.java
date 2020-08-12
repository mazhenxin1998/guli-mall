package com.mzx.gulimall.product.dao;

import com.mzx.gulimall.product.entity.AttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzx.gulimall.product.entity.SkuEsModel;
import com.mzx.gulimall.product.vo.Attr;
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
public interface AttrDao extends BaseMapper<AttrEntity> {

    /**
     * 通过分类ID查询该分类下所关联的所有规格参数属性.
     * @param catalogId
     * @return
     */
    List<Attr> findAttrsByCatalogId(@Param(value = "catalogId") Long catalogId);
	
}
