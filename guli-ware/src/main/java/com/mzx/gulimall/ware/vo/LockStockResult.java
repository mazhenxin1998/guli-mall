package com.mzx.gulimall.ware.vo;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-09 16:16 周五.
 */
public class LockStockResult {

    /**
     * 当前锁定库存的SKU的ID.
     */
    private Long skuId;
    /**
     * 当前skuId所锁定的库存数量.
     */
    private Integer num;
    /**
     * 锁定库存是否成功.
     */
    private Boolean locked;

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

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }
}
