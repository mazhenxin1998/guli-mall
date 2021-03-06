package com.mzx.gulimall.common.mq;

import lombok.Data;
import lombok.ToString;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-12 21:15 周一.
 */
@Data
public class StockLockTo {

    private String orderSn;

    /**
     * 工作单的ID.
     * 一个工作单可能对应着多个工作详情ID.
     */
    private Long id;
    /**
     * 工作详情ID.
     */
    private StockDetailTo detail;

    @Override
    public String toString() {
        return "StockLockTo{" +
                "orderSn='" + orderSn + '\'' +
                ", id=" + id +
                ", detail=" + detail +
                '}';
    }
}
