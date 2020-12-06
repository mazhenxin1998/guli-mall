package com.mzx.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.coupon.entity.SmsSeckillSessionEntity;
import com.mzx.gulimall.coupon.vo.ResultVo;
import com.mzx.gulimall.coupon.vo.SecKillResultVo;

import java.util.Map;

/**
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 17:45:21
 */
public interface SmsSeckillSessionService extends IService<SmsSeckillSessionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 分页查询所有要秒杀.
     *
     * @param params
     * @return
     */
    SecKillResultVo listSecKillsPage(Map<String, Object> params);

    /**
     * 返回最近三天内要上架的响应数据.
     * <p>
     * 同时也需要将该次最近活动关联的商品的SkuInfo信息也设置上去.
     *
     * @return
     */
    ResultVo<SmsSeckillSessionEntity> getLatesSession();

}

