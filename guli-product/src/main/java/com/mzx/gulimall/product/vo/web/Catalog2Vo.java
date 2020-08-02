package com.mzx.gulimall.product.vo.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author ZhenXinMa
 * @date 2020/8/2 13:37
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Catalog2Vo {

    /**
     * 当前分类为二级分类，一级分类ID为catalog1ID.
     */
    private String catalog1Id;
    private List<Catalog3Vo> catalog3List;
    private String id;
    private String name;

    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Catalog3Vo {

        /**
         * 父分类的ID为二级分类的ID。
         */
        private String catalog2Id;
        private String id;
        private String name;

    }

}
