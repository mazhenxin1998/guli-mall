package com.mzx.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.ware.entity.WareOrderTaskDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * 库存工作单
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-25 14:15:39
 */
public interface WareOrderTaskDetailService extends IService<WareOrderTaskDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据库存工作单查询该工作单所关联的所有库存工作单详情.
     *
     * @param stockId
     * @return
     */
    List<WareOrderTaskDetailEntity> getOrderTaskDetailsByStockId(Long stockId);

    /**
     * 对库存锁定详情单进行修改.
     * @param collect
     */
    void updateTaskDetails(List<Long> collect);
}

