package com.mzx.gulimall.coupon.controller;

import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.coupon.entity.SmsSpuBoundsEntity;
import com.mzx.gulimall.coupon.service.SmsSpuBoundsService;
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
@RequestMapping("coupon/smsspubounds")
public class SmsSpuBoundsController {
    @Autowired
    private SmsSpuBoundsService smsSpuBoundsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = smsSpuBoundsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        SmsSpuBoundsEntity smsSpuBounds = smsSpuBoundsService.getById(id);

        return R.ok().put("smsSpuBounds", smsSpuBounds);
    }

    /**
     * 保存 其对应着product的feign接口.
     */
    @PostMapping("/save")
    public R save(@RequestBody SmsSpuBoundsEntity smsSpuBounds) {

        smsSpuBoundsService.save(smsSpuBounds);
//        System.out.println("测试远程调用: "+smsSpuBounds.toString());
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SmsSpuBoundsEntity smsSpuBounds) {
        smsSpuBoundsService.updateById(smsSpuBounds);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        smsSpuBoundsService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
