package com.mzx.gulimall.product.vo;

import com.mzx.gulimall.product.valid.AttrGroupRelation;

import javax.validation.constraints.NotEmpty;

/**
 * @author ZhenXinMa
 * @date 2020/7/20 13:26
 */
public class AttrRelationVo {

    @NotEmpty(message = "保存属性和属性组之间的关系时上传的属性ID不能为空", groups = {AttrGroupRelation.class})
    private Long attrId;

    @NotEmpty(message = "保存属性和属性组之间的关系时上传的分组ID不能为空", groups = {AttrGroupRelation.class})
    private Long attrGroupId;

    public Long getAttrId() {
        return attrId;
    }

    public void setAttrId(Long attrId) {
        this.attrId = attrId;
    }

    public Long getAttrGroupId() {
        return attrGroupId;
    }

    public void setAttrGroupId(Long attrGroupId) {
        this.attrGroupId = attrGroupId;
    }
}
