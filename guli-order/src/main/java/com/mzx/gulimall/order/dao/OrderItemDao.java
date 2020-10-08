package com.mzx.gulimall.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzx.gulimall.order.entity.OrderItemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单项信息
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 16:44:36
 */
@Mapper
@Repository
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {

}
