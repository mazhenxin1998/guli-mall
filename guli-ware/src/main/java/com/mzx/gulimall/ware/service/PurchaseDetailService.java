package com.mzx.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.ware.entity.PurchaseDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-25 14:15:39
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 商品库存的查询.
     *
     * @param params
     * @return
     */
    PageUtils queryPageDedatils(Map<String, Object> params);

    /**
     * 根据采购单的ID查出该采购单相关联的采购需求.
     *
     * @param id
     * @return
     */
    List<PurchaseDetailEntity> findPurchaseDetailsByPurchaseId(Long id);
}

