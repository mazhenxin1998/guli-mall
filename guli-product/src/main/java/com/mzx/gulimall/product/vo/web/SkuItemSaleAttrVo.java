package com.mzx.gulimall.product.vo.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author ZhenXinMa
 * @date 2020/8/19 18:20
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SkuItemSaleAttrVo{

    private Long attrId;
    private String attrName;
    /**
     * 是多个值.
     */
    private String attrValues;

}
