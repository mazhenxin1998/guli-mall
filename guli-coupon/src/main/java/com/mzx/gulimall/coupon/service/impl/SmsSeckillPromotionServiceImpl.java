package com.mzx.gulimall.coupon.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.Query;

import com.mzx.gulimall.coupon.dao.SmsSeckillPromotionDao;
import com.mzx.gulimall.coupon.entity.SmsSeckillPromotionEntity;
import com.mzx.gulimall.coupon.service.SmsSeckillPromotionService;


@Service("smsSeckillPromotionService")
public class SmsSeckillPromotionServiceImpl extends ServiceImpl<SmsSeckillPromotionDao, SmsSeckillPromotionEntity> implements SmsSeckillPromotionService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SmsSeckillPromotionEntity> page = this.page(
                new Query<SmsSeckillPromotionEntity>().getPage(params),
                new QueryWrapper<SmsSeckillPromotionEntity>()
        );

        return new PageUtils(page);
    }

}