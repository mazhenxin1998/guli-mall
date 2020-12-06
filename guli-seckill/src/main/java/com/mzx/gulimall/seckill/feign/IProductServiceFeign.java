package com.mzx.gulimall.seckill.feign;

import com.mzx.gulimall.seckill.vo.ResultVo;
import com.mzx.gulimall.seckill.vo.SkuInfoEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-12-01 18:41 周二.
 */
@FeignClient(value = "gulimall-product")
public interface IProductServiceFeign {


    /**
     * 远程查询商品的详细信息.
     *
     * @param id 要查询商品详细信息的SkuId.
     * @return
     */
    @RequestMapping("product/skuinfo/get/{id}")
    ResultVo<SkuInfoEntity> getSkuInfo(@PathVariable("id") Long id);

}
