package com.mzx.gulimall.coupon.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzx.gulimall.coupon.entity.SmsSeckillSessionEntity;
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
public interface SmsSeckillSessionDao extends BaseMapper<SmsSeckillSessionEntity> {

    /**
     * 分页查询.
     *
     * @param page
     * @param size
     * @return
     */
    List<SmsSeckillSessionEntity> listSeckillSessions(@Param("page") Integer page, @Param("size") Integer size);

    /**
     * 按照时间段从DB中取出以当前时间点为基准的最近三天要上架的商品.
     *
     * @param starterTime 基准开始时间.
     * @param endTime     基准结束时间。
     * @return
     */
    List<SmsSeckillSessionEntity> getLatesSession(@Param("starterTime") String starterTime, @Param("endTime") String endTime);


}
