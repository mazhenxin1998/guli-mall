package com.mzx.gulimall.coupon.dao;

import com.mzx.gulimall.coupon.entity.SmsSeckillPromotionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzx.gulimall.coupon.entity.SmsSeckillSkuRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * 
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 17:45:21
 */
@Mapper
@Repository
public interface SmsSeckillPromotionDao extends BaseMapper<SmsSeckillPromotionEntity> {

    /**
     * 根据条件查询.
     * @param promotion 查询条件.
     * @return
     */
    List<SmsSeckillSkuRelationEntity> listSeckillPromotions(@Param("promotion") Integer promotion);
}
