package com.mzx.gulimall.coupon.controller;

import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.coupon.entity.SmsSkuFullReductionEntity;
import com.mzx.gulimall.coupon.service.SmsSkuFullReductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 17:45:21
 */
@RestController
@RequestMapping("coupon/smsskufullreduction")
public class SmsSkuFullReductionController {
    @Autowired
    private SmsSkuFullReductionService smsSkuFullReductionService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = smsSkuFullReductionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        SmsSkuFullReductionEntity smsSkuFullReduction = smsSkuFullReductionService.getById(id);

        return R.ok().put("smsSkuFullReduction", smsSkuFullReduction);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SmsSkuFullReductionEntity smsSkuFullReduction) {
        smsSkuFullReductionService.save(smsSkuFullReduction);

        return R.ok();
    }

    @RequestMapping(value = "/batch/save")
    public R saveBatchSkuFullReduction(@RequestBody List<SmsSkuFullReductionEntity> entities) {

        smsSkuFullReductionService.saveBatch(entities);
//        System.out.println(entities.toString());
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SmsSkuFullReductionEntity smsSkuFullReduction) {
        smsSkuFullReductionService.updateById(smsSkuFullReduction);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        smsSkuFullReductionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
