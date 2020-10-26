package com.mzx.gulimall.coupon.controller;

import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.coupon.entity.SmsSeckillPromotionEntity;
import com.mzx.gulimall.coupon.service.SmsSeckillPromotionService;
import com.mzx.gulimall.coupon.vo.SecKillResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * http://localhost:88/api/coupon/seckillskurelation/list?t=1603720630953&page=1&limit=10&key=&promotionSessionId=1
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 17:45:21
 */
@RestController
@RequestMapping("coupon/seckillpromotion")
public class SmsSeckillPromotionController {
    @Autowired
    private SmsSeckillPromotionService smsSeckillPromotionService;

    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {

        // 应该是修改这里.
        PageUtils page = smsSeckillPromotionService.queryPage(params);
        return R.ok().put("page", page);

    }

    /**
     * 列表.
     * http://localhost:88/api/coupon/seckillskurelation/list?t=1603719434423&page=1&limit=10&key=&promotionSessionId=1
     * 请求方法: GET
     */
//    @RequestMapping("/list")
//    public SecKillResultVo list(@RequestParam Map<String, Object> params) {
//
//        System.out.println("list方法发生了.");
//        // 这里应该根据
//        // 返回值又的自己封装.
//        return smsSeckillPromotionService.listSeckillPromotion(params);
//
//    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        SmsSeckillPromotionEntity smsSeckillPromotion = smsSeckillPromotionService.getById(id);

        return R.ok().put("smsSeckillPromotion", smsSeckillPromotion);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SmsSeckillPromotionEntity smsSeckillPromotion) {
        smsSeckillPromotionService.save(smsSeckillPromotion);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SmsSeckillPromotionEntity smsSeckillPromotion) {
        smsSeckillPromotionService.updateById(smsSeckillPromotion);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        smsSeckillPromotionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
