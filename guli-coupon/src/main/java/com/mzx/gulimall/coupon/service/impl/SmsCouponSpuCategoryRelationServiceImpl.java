package com.mzx.gulimall.coupon.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.Query;

import com.mzx.gulimall.coupon.dao.SmsCouponSpuCategoryRelationDao;
import com.mzx.gulimall.coupon.entity.SmsCouponSpuCategoryRelationEntity;
import com.mzx.gulimall.coupon.service.SmsCouponSpuCategoryRelationService;


@Service("smsCouponSpuCategoryRelationService")
public class SmsCouponSpuCategoryRelationServiceImpl extends ServiceImpl<SmsCouponSpuCategoryRelationDao, SmsCouponSpuCategoryRelationEntity> implements SmsCouponSpuCategoryRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SmsCouponSpuCategoryRelationEntity> page = this.page(
                new Query<SmsCouponSpuCategoryRelationEntity>().getPage(params),
                new QueryWrapper<SmsCouponSpuCategoryRelationEntity>()
        );

        return new PageUtils(page);
    }

}