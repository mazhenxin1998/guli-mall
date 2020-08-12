package com.mzx.gulimall.search.vo;

import com.mzx.gulimall.search.entity.SkuEsModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author ZhenXinMa
 * @date 2020/8/12 17:09
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SearchResult {

    private List<SkuEsModel> products;

    /**
     * 当前页码.
     */
    private Integer pageNum;
    /**
     * 该次查询的总记录数.
     */
    private Long total;
    /**
     * 当前查询的总的页码数.
     */
    private Integer totalPages;
    /**
     * 当前查询所涉及到的所有品牌.
     */
    private List<BrandVo> brands;
    /**
     * 当前所查询结果中所包含的所有属性.
     */
    private List<AttrVo> attrs;
    /**
     * 当前所查询结果中所包含的所有分类信息.
     */
    private List<CatalogVo> catalogs;

    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BrandVo {

        private Long brandId;
        private String brandName;
        private String brandImg;
    }

    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttrVo {

        private Long attrId;
        private String attrName;
        private List<String> attrValue;

    }

    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CatalogVo {

        private Long catalogId;
        private String catalogName;

    }


}
