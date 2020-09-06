package com.mzx.gulimall.cart.feign;

import com.mzx.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 服务之间的调用,现在还没有解决服务熔断以及服务降级.
 *
 * @author ZhenXinMa
 * @date 2020/9/6 16:22
 */
@FeignClient(value = "gulimall-product")
public interface ProductServiceFeign {

    /**
     * 通过skuID查询出该SKU的基本信息.
     *
     * @param skuId
     * @return
     */
    @RequestMapping("/product/skuinfo/info/{skuId}")
    R info(@PathVariable("skuId") Long skuId);

    /**
     * 通过id查询出该SKU所属于的所有销售属性》
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/product/skusaleattrvalue/find/{id}")
    R findSkuSaleAttr(@PathVariable(value = "id") Long id);


}
