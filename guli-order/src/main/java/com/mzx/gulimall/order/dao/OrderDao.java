package com.mzx.gulimall.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzx.gulimall.order.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 订单
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 16:44:36
 */
@Mapper
@Repository
public interface OrderDao extends BaseMapper<OrderEntity> {

    /**
     * 根据订单号查询订单.
     *
     * @param orderSn
     * @return
     */
    OrderEntity getOrderByOrderSn(@Param("orderSn") String orderSn);
}
