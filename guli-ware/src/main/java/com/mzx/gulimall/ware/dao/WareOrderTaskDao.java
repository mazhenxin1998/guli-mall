package com.mzx.gulimall.ware.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzx.gulimall.ware.entity.WareOrderTaskEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 库存工作单
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-25 14:15:39
 */
@Mapper
@Repository
public interface WareOrderTaskDao extends BaseMapper<WareOrderTaskEntity> {

    /**
     * 根据订单号查询WareOrderTaskEntity.
     *
     * @param orderSn
     * @return
     */
    WareOrderTaskEntity getOrderTaskByOrderSn(@Param("orderSn") String orderSn);
}
