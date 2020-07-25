package com.mzx.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.ware.entity.PurchaseEntity;
import com.mzx.gulimall.ware.vo.PurchaseMergeVo;

import java.util.Map;

/**
 * 采购信息
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-25 14:15:39
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 动态根据条件进行查询.
     *
     * @param params
     * @return
     */
    PageUtils queryPageDetails(Map<String, Object> params);

    /**
     * 查询新建状态的采购或者是未完成的采购单.
     * @param params
     * @return
     */
    PageUtils queryPurchaseDetails(Map<String, Object> params);

    /**
     * 采购需求的合并.
     * @param vo
     * @return
     */
    R saveMerge(PurchaseMergeVo vo);
}

