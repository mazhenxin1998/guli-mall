package com.mzx.gulimall.search.controller;

import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.search.entity.SkuEsModel;
import com.mzx.gulimall.search.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ZhenXinMa
 * @date 2020/8/1 15:54
 */
@RestController
@RequestMapping(value = "/search")
public class SearchController {

    @Autowired
    private ISearchService iSearchService;

    @PostMapping(value = "/save")
    public R saveProduct(@RequestBody List<SkuEsModel> skuEsModel) {

        // TODO: 将模型数据存入gulimall-product索引库中
        boolean b = iSearchService.saveProduct(skuEsModel);
        // TODO: 这里应该做的判断完全放在service里面进行比较的.
        if (b) {

            return R.ok();
        } else {

            return R.error();
        }
    }


}
