/**
 * Copyright 2019 bejson.com
 */
package com.mzx.gulimall.product.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 积分信息.
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class Bounds {

    /**
     * 购买该物件所需要的金币信息
     */
    private BigDecimal buyBounds;

    /**
     * 购买该物件所获得会员积分.
     */
    private BigDecimal growBounds;


}