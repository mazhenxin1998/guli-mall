package com.mzx.gulimall.ware.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzx.gulimall.ware.entity.WareOrderTaskDetailEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 库存工作单
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-25 14:15:39
 */
@Mapper
@Repository
public interface WareOrderTaskDetailDao extends BaseMapper<WareOrderTaskDetailEntity> {

    /**
     * 根据库存工作单查询该库存工作单所关联的所有库存工作详情。
     *
     * @param stockId 库存工作单的ID.
     * @return
     */
    List<WareOrderTaskDetailEntity> getOrderTaskDetailsByStockId(@Param("stockId") Long stockId);

    /**
     * 修改当前库存工作详情单的锁定状态为已解锁.
     *
     * @param status            要修改的库存工作详情单的状态.
     * @param orderTaskDetailId
     */
    void updateLockStatus(@Param("orderTaskDetailId") Long orderTaskDetailId, @Param("status") Long status);
}
