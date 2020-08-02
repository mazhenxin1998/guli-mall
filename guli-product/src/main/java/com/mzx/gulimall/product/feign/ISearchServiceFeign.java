package com.mzx.gulimall.product.feign;

import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.product.entity.SkuEsModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author ZhenXinMa
 * @date 2020/8/1 15:54
 */
@FeignClient(value = "gulimall-search")
public interface ISearchServiceFeign {


    @PostMapping(value = "/search/save")
    R saveProduct(@RequestBody List<SkuEsModel> skuEsModel);

}
