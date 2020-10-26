package com.mzx.gulimall.coupon.controller;

import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.coupon.entity.SmsSeckillSkuRelationEntity;
import com.mzx.gulimall.coupon.service.SmsSeckillPromotionService;
import com.mzx.gulimall.coupon.service.SmsSeckillSkuRelationService;
import com.mzx.gulimall.coupon.vo.SecKillResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 17:45:21
 */
@RestController
@RequestMapping("coupon/seckillskurelation")
public class SmsSeckillSkuRelationController {
    @Autowired
    private SmsSeckillSkuRelationService smsSeckillSkuRelationService;

    @Autowired
    private SmsSeckillPromotionService smsSeckillPromotionService;

    /**
     * 列表
     */
//    @RequestMapping("/list")
//    public R list(@RequestParam Map<String, Object> params) {
//
//        // 应该是修改这里.
//        PageUtils page = smsSeckillSkuRelationService.queryPage(params);
//        return R.ok().put("page", page);
//
//    }

    @RequestMapping("/list")
    public SecKillResultVo list(@RequestParam Map<String, Object> params) {

        // 应该是修改这里.
        PageUtils page = smsSeckillSkuRelationService.queryPage(params);
        SecKillResultVo resultVo = smsSeckillPromotionService.listSeckillPromotion(params);
        return resultVo;

    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        SmsSeckillSkuRelationEntity smsSeckillSkuRelation = smsSeckillSkuRelationService.getById(id);

        return R.ok().put("smsSeckillSkuRelation", smsSeckillSkuRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SmsSeckillSkuRelationEntity smsSeckillSkuRelation) {
        smsSeckillSkuRelationService.save(smsSeckillSkuRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SmsSeckillSkuRelationEntity smsSeckillSkuRelation) {
        smsSeckillSkuRelationService.updateById(smsSeckillSkuRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        smsSeckillSkuRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
