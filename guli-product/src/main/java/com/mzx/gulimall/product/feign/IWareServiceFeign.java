package com.mzx.gulimall.product.feign;

import com.mzx.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ZhenXinMa
 * @date 2020/8/1 14:22
 */
@FeignClient(value = "gulimall-ware")
public interface IWareServiceFeign {

    @RequestMapping("/ware/waresku/info/{id}")
    R info(@PathVariable("id") Long id);

    @GetMapping(value = "/ware/waresku/get/{skuId}")
    boolean getStock(@PathVariable(value = "skuId") Long skuId);


}
