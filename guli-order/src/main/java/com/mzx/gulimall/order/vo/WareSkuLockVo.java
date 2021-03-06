package com.mzx.gulimall.order.vo;

import java.util.List;

/**
 * 远程查询库存封装的实体类.
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-06 16:39 周二.
 */
public class WareSkuLockVo {

    /**
     * 订单序列化.
     */
    String orderSn;

    /**
     * 需要锁定的库存.
     * 根据每一个SkuId的count数量进行SKU库存锁定.
     */
    List<Item> locks;

    /**
     * 我不需要设定WareId.
     */
    public String getOrderSn() {
        return this.orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public List<Item> getLocks() {
        return this.locks;
    }

    public void setLocks(List<Item> locks) {
        this.locks = locks;
    }

    public static class Item {

        /**
         * 要锁定的库存的skuId.
         */
        Long skuId;
        /**
         * 锁定sku库存的数量.
         */
        Integer num;

        String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Long getSkuId() {
            return skuId;
        }

        public void setSkuId(Long skuId) {
            this.skuId = skuId;
        }

        public Integer getNum() {
            return num;
        }

        public void setNum(Integer num) {
            this.num = num;
        }
    }

}
