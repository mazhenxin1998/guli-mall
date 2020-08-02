package com.mzx.gulimall.ware.feign;

import com.mzx.gulimall.ware.entity.SkuInfoEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ZhenXinMa
 * @date 2020/7/31 15:26
 */
@FeignClient(value = "gulimall-product")
public interface ProductServiceFeign {


    @GetMapping(value = "/product/skuinfo/get/name")
    SkuInfoEntity getSkuName(@RequestParam(value = "id") Long id);


}
