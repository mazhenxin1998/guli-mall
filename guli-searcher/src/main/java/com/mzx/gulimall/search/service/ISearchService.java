package com.mzx.gulimall.search.service;

import com.mzx.gulimall.search.entity.SkuEsModel;

import java.util.List;

/**
 * @author ZhenXinMa
 * @date 2020/8/1 16:20
 */
public interface ISearchService {

    /**
     * 批量将SKU存入数据库中.
     *
     * @param models
     */
    boolean saveProduct(List<SkuEsModel> models);


}
