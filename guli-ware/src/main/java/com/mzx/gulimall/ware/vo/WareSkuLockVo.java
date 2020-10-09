package com.mzx.gulimall.ware.vo;

import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-09 16:42 周五.
 */
@ToString
public class WareSkuLockVo {

    /**
     * 订单序列化.
     */
    @NotEmpty(message = "订单号不能为空.")
    private String orderSn;

    /**
     * 需要锁定的库存.
     * 根据每一个SkuId的count数量进行SKU库存锁定.
     */
    @NotNull
    private List<Item> locks;

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public List<Item> getLocks() {
        return locks;
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

        Long wareId;

        public Long getWareId() {
            return wareId;
        }

        public void setWareId(Long wareId) {
            this.wareId = wareId;
        }

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
