package com.mzx.gulimall.cart.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author ZhenXinMa.
 * @slogan 浮生若梦, 若梦非梦, 浮生何梦? 如梦之梦.
 * @date 2020/9/13 17:57.
 */
@Data
@ToString
@AllArgsConstructor
public class TotalCountTo {

    /**
     * 购物车总共的数量.
     */
    private Integer count;
    /**
     * 购物车总的种类数量.
     */
    private Integer typeCount;

    /**
     * 当前购物车的总价格.
     */
    private BigDecimal totalPrice;

    /**
     * 每当创建出一个该类型的对象,那么其属性默认值都是0.
     * 其实不这么赋值也是没有很多大的关系的,那么就要使用其基本类型. 而Integer这样的包装类型的零值是null.
     */
    public TotalCountTo() {

        this.count = 0;
        this.typeCount = 0;
        this.totalPrice = new BigDecimal(0);
    }

}
