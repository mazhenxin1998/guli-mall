package com.mzx.gulimall.cart.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author ZhenXinMa
 * @date 2020/9/6 15:50
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CartParamVo {

    /*
     * --------------------------------------------------------
     * 使用JSR303进行参数校验的时候,不能使用@NotEmpty来校验Long类型.
     * --------------------------------------------------------
     * */
    
    @NotNull(message = "上传的商品SKUID为空,请检查之后重新添加")
    private Long skuId;
    @NotNull(message = "要添加的数量小于0,请检查之后重新添加.")
    private Integer count;

}
