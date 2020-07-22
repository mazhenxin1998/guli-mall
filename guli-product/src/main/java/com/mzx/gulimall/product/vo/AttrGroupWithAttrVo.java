package com.mzx.gulimall.product.vo;

import com.mzx.gulimall.product.entity.AttrEntity;
import lombok.Data;

import java.security.PrivateKey;
import java.util.List;

/**
 * @author ZhenXinMa
 * @date 2020/7/20 15:28
 */
@Data
public class AttrGroupWithAttrVo {

    private Long attrGroupId;

    private String attrGroupName;

    private int sort;

    private String descript;

    private String icon;

    private Long catelogId;

    List<AttrEntity> attrs;

    public List<AttrEntity> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<AttrEntity> attrs) {
        this.attrs = attrs;
    }
}
