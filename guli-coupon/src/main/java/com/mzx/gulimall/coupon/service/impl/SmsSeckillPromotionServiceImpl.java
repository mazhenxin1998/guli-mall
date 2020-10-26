package com.mzx.gulimall.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.Query;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.coupon.dao.SmsSeckillPromotionDao;
import com.mzx.gulimall.coupon.entity.SmsSeckillPromotionEntity;
import com.mzx.gulimall.coupon.entity.SmsSeckillSkuRelationEntity;
import com.mzx.gulimall.coupon.service.SmsSeckillPromotionService;
import com.mzx.gulimall.coupon.vo.SecKillResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("smsSeckillPromotionService")
public class SmsSeckillPromotionServiceImpl extends ServiceImpl<SmsSeckillPromotionDao, SmsSeckillPromotionEntity> implements SmsSeckillPromotionService {

    @Autowired
    private SmsSeckillPromotionDao smsSeckillPromotionDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SmsSeckillPromotionEntity> page = this.page(
                new Query<SmsSeckillPromotionEntity>().getPage(params),
                new QueryWrapper<SmsSeckillPromotionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public SecKillResultVo listSeckillPromotion(Map<String, Object> params) {

        // TODO: 2020/10/26 BUG:就只有ID有值?
        System.out.println("listSeckillPromotion Service 方法发生了. ");
        Integer promotion = Integer.valueOf(params.get("promotionSessionId").toString());
        List<SmsSeckillSkuRelationEntity> entities = smsSeckillPromotionDao.listSeckillPromotions(promotion);
        SecKillResultVo resultVo = new SecKillResultVo();
        SecKillResultVo.Page<SmsSeckillSkuRelationEntity> page = new SecKillResultVo.Page<>();
        page.setList(entities);
        resultVo.setCode(0);
        resultVo.setPage(page);
        return resultVo;

    }

}