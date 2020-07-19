package com.mzx.gulimall.product.dao;

import com.mzx.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 12:26:27
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

    /**
     * 根据分组ID查出其下的所有属性ID.
     *
     * @param groupId
     * @return
     */
    List<Long> selectAttrIds(Long groupId);

}
