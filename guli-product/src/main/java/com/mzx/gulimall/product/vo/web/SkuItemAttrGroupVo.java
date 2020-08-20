package com.mzx.gulimall.product.vo.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author ZhenXinMa
 * @date 2020/8/19 20:32
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public  class SkuItemAttrGroupVo{

    private String groupName;
    private List<SpuBaseAttrVo> attrs;

}
