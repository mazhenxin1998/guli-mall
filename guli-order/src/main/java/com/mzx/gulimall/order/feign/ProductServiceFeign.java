package com.mzx.gulimall.order.feign;

import com.mzx.gulimall.order.vo.SpuInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-04 21:28 周日.
 */
@FeignClient(value = "gulimall-product")
public interface ProductServiceFeign {

    /**
     * 根据SkuId查询出SPU的详细信息.
     *
     * @param skuId
     * @return
     */
    @GetMapping(value = "/product/spuinfo/get/spuinfo/{skuId}")
    String getSpuInfoEntityBySkuId(@PathVariable(value = "skuId") Long skuId);


}
