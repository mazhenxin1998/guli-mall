package com.mzx.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mzx.gulimall.common.order.OrderTo;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.ware.entity.WareOrderTaskDetailEntity;
import com.mzx.gulimall.ware.entity.WareSkuEntity;
import com.mzx.gulimall.ware.vo.LockStockResult;
import com.mzx.gulimall.ware.vo.WareSkuLockVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-25 14:15:39
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 对商品库存的条件查询.
     *
     * @param params
     * @return
     */
    PageUtils queryPageDetails(Map<String, Object> params);

    /**
     * 根据指定的SKUID查询出该ID是否有存货.
     *
     * @param skuId
     * @return
     */
    boolean getStock(Long skuId);

    /**
     * 批量查询库存数量.
     *
     * @param ids
     * @return
     */
    R getListStock(List<Long> ids);

    /**
     * 将给定的skuId和数量的库存进行锁定.
     *
     * @param wareSkuLockVo
     * @return
     */
    List<LockStockResult> lockStock(WareSkuLockVo wareSkuLockVo);

    /**
     * 将参数中的集合中对应的库存工作单详情所对应的库存锁定情况进行回滚.
     * <p>
     * 回滚失败需要将MQ消息重新打回MQ并且重新消费.
     * 并且要回滚库存工作详情单会回滚SKU也会回滚MQ也将回滚. 然后对该库存重新进行解锁.
     *
     * @param detailEntities
     */
    void listReleaseStocks(List<WareOrderTaskDetailEntity> detailEntities);

    /**
     * 对库存进行回滚.
     *
     * @param order
     */
    void listReleaseStocks(OrderTo order);
}

