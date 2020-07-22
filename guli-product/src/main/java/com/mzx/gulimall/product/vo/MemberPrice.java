/**
 * Copyright 2019 bejson.com
 */
package com.mzx.gulimall.product.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Auto-generated: 2019-11-26 10:50:34
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class MemberPrice {

    /**
     * 应该是SKU的ID.
     */
    private Long id;
    /**
     * 会员名字.
     */
    private String name;
    /**
     * 会员价格.
     */
    private BigDecimal price;

}