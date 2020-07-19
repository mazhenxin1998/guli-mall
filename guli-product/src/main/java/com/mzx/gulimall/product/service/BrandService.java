package com.mzx.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.product.entity.BrandEntity;
import com.mzx.gulimall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * Ʒ
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 12:26:27
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);


    /**
     * 在修改品牌信息的时候，如果其修改的字段是name，那么应该需要将分类与品牌的中间表的信息也同时进行修改.
     *
     * @param brand
     */
    void updateDetail(BrandEntity brand);
}

