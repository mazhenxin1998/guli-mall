package com.mzx.gulimall.search.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author ZhenXinMa
 * @date 2020/8/12 16:35
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class SearchParam {

    /**
     * 页面传过来的全文匹配条件.
     */
    private String keyword;
    /**
     * 分类ID.
     */
    private Long catalog3Id;
    /**
     * 排序条件.
     * 样例:
     * sort=saleCount_asc/desc
     * // asc: 升序 desc降序.
     */
    private String sort;

    /**
     * 过滤条件: 0无货 1 有货.
     */
    private Integer hasStock;
    /**
     * 价格区间查询.
     */
    private String skuPrice;
    /**
     * 过滤条件: 根据多个品牌进行过滤.
     */
    private List<Long> brandId;
    /**
     * 过滤条件: 按照规格参数属性进行筛选.
     */
    private List<String> attrs;
    /**
     * 当前查询的页码.
     */
    private Integer pageNum;

}
