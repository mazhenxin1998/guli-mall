package com.mzx.gulimall.product.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ZhenXinMa
 * @date 2020/8/1 13:29
 */
@Data
public class SkuEsModel {


    private Long spuId;
    private String spuName;

    private Long skuId;
    private String skuTitle;
    private BigDecimal skuPrice;
    private String skuImg;
    private Long saleCount;
    private boolean hasStock;
    private Long hotScore;
    private Long brandId;
    private String brandName;
    private String brandImg;
    private Long catalogId;
    private String catalogName;

    private List<Attrs> attrs;

    @Data
    public static class Attrs {

        private Long attrId;
        private String attrName;
        private String attrValue;

    }

}
