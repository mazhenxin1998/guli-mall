package com.mzx.gulimall.product.vo;

/**
 * @author ZhenXinMa
 * @date 2020/7/20 15:02
 */
public class CategoryBrandRelationVo {

    private Long brandId;

    private String brandName;

    public CategoryBrandRelationVo(Long brandId, String brandName) {
        this.brandId = brandId;
        this.brandName = brandName;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
