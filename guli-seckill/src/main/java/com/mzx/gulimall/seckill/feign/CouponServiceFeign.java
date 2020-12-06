package com.mzx.gulimall.seckill.feign;

import com.mzx.gulimall.seckill.vo.ResultVo;
import com.mzx.gulimall.seckill.vo.SeckillSessionEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-12-01 16:09 周二.
 */
@FeignClient(value = "gulimall-coupon")
public interface CouponServiceFeign {

    /**
     * 调用远程服务,查询出来的结果是已经封装好的结果.
     *
     * @return
     */
    @GetMapping(value = "/coupon/seckillsession/get/lates/three/days/session")
    ResultVo<SeckillSessionEntity> getLatesSession();

}
