package com.mzx.gulimall.coupon.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzx.gulimall.coupon.entity.SmsSeckillSkuRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 17:45:21
 */
@Mapper
@Repository
public interface SmsSeckillSkuRelationDao extends BaseMapper<SmsSeckillSkuRelationEntity> {

    /**
     * 根据会话id查询该会话所关联的所有商品.
     *
     * @param id
     * @return
     */
    List<SmsSeckillSkuRelationEntity> getById(@Param("id") Long id);
}
