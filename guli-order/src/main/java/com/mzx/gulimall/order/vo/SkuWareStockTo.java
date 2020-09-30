package com.mzx.gulimall.order.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-30 15:40 周三.
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SkuWareStockTo {

    private Long stock;
    private Long wareId;
    private Long skuId;

}
