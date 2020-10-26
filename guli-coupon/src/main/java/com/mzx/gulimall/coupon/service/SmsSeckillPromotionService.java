package com.mzx.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.coupon.entity.SmsSeckillPromotionEntity;
import com.mzx.gulimall.coupon.vo.SecKillResultVo;

import java.util.Map;

/**
 * 
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 17:45:21
 */
public interface SmsSeckillPromotionService extends IService<SmsSeckillPromotionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据promotionSessionId查询出相互关联的数据.
     * @param params
     * @return
     */
    SecKillResultVo listSeckillPromotion(Map<String, Object> params);
}

