package com.mzx.gulimall.ware.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.awt.*;

/**
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
