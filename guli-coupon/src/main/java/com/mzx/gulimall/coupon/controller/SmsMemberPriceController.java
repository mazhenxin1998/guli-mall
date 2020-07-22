package com.mzx.gulimall.coupon.controller;

import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.coupon.entity.SmsMemberPriceEntity;
import com.mzx.gulimall.coupon.service.SmsMemberPriceService;
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
@RequestMapping("coupon/smsmemberprice")
public class SmsMemberPriceController {
    @Autowired
    private SmsMemberPriceService smsMemberPriceService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = smsMemberPriceService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        SmsMemberPriceEntity smsMemberPrice = smsMemberPriceService.getById(id);

        return R.ok().put("smsMemberPrice", smsMemberPrice);
    }

    /**
     * 保存： 单个保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SmsMemberPriceEntity smsMemberPrice) {

        smsMemberPriceService.save(smsMemberPrice);

        return R.ok();
    }

    @RequestMapping(value = "/batch/save")
    public R saveBatch(@RequestBody List<SmsMemberPriceEntity> entities) {

        /*批量的进行保存.*/
        smsMemberPriceService.saveBatch(entities);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SmsMemberPriceEntity smsMemberPrice) {
        smsMemberPriceService.updateById(smsMemberPrice);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        smsMemberPriceService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
