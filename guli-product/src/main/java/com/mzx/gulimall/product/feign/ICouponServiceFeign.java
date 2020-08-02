package com.mzx.gulimall.product.feign;

import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.product.entity.SmsMemberPriceEntity;
import com.mzx.gulimall.product.entity.SmsSkuFullReductionEntity;
import com.mzx.gulimall.product.entity.SmsSkuLadderEntity;
import com.mzx.gulimall.product.entity.SmsSpuBoundsEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 实现fallback.
 *
 * @author ZhenXinMa
 * @date 2020/7/22 15:35
 */
@FeignClient(value = "gulimall-coupon")
public interface ICouponServiceFeign {

    @PostMapping("coupon/smsspubounds/save")
    R save(@RequestBody SmsSpuBoundsEntity smsSpuBounds);

    /**
     * 进行会员优惠信息的保存.
     *
     * @param smsMemberPriceEntity
     * @return
     */
    @RequestMapping(value = "coupon/smsmemberprice/save")
    R saveMemberPrice(@RequestBody SmsMemberPriceEntity smsMemberPriceEntity);

    /**
     * 批量保存.
     *
     * @param entities
     * @return
     */
    @RequestMapping(value = "coupon/smsmemberprice/batch/save")
    R saveMemberPriceBatch(@RequestBody List<SmsMemberPriceEntity> entities);

    /**
     * 保存满多少减一定的钱.
     *
     * @param entity
     * @return
     */
    @RequestMapping(value = "coupon/smsskufullreduction/save")
    R saveFullPriceReducePrice(@RequestBody SmsSkuFullReductionEntity entity);

    @RequestMapping(value = "coupon/smsskuladder/save")
    R saveSkuLadder(@RequestBody SmsSkuLadderEntity smsSkuLadderEntity);


    @RequestMapping(value = "/test/t1")
    String t1();

}
