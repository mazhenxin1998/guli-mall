package com.mzx.gulimall.ware.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 锁定库存接受的参数应该是数组形式.
 * <p>
 * 传过去的参数最好全部是skuId.
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-28 17:10 周一.
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
