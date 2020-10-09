package com.mzx.gulimall.ware.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzx.gulimall.ware.entity.WareSkuEntity;
import com.mzx.gulimall.ware.vo.SkuWareStockTo;
import com.mzx.gulimall.ware.vo.WareSkuLockVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品库存
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-25 14:15:39
 */
@Mapper
@Repository
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    /**
     * 批量查询sku的库存数量.
     *
     * @param ids
     * @return
     */
    List<SkuWareStockTo> listFindStock(@Param(value = "ids") List<Long> ids);

    /**
     * 锁定库存.
     *
     * @param item
     */
    void lockStock(@Param("item") WareSkuLockVo.Item item);

    /**
     * 查询从哪个仓库进行查询.
     *
     * @param skuId
     * @return
     */
    List<Integer> findWhereWareHaveStocks(@Param("skuId") Long skuId);
}
