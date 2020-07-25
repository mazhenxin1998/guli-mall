package com.mzx.gulimall.ware.controller;

import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.ware.entity.PurchaseEntity;
import com.mzx.gulimall.ware.service.PurchaseService;
import com.mzx.gulimall.ware.vo.PurchaseMergeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;


/**
 * 采购单.
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-25 14:15:39
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {

        PageUtils page = purchaseService.queryPageDetails(params);
        return R.ok().put("page", page);
    }

    /**
     * 在合并采购需求的时候进行采购单的查询.
     * <p>
     * 该查询的只是新建状态或者是未完成状态的采购单.
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/unreceive/list")
    public R unreceiveList(@RequestParam Map<String, Object> params) {

        PageUtils pageUtils = purchaseService.queryPurchaseDetails(params);
        return R.ok().put("data", pageUtils);
    }

    @RequestMapping(value = "/merge")
    public R merge(@RequestBody PurchaseMergeVo vo) {

        return purchaseService.saveMerge(vo);

    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody PurchaseEntity purchase) {
        Date date = new Date();
        purchase.setCreateTime(date);
        purchase.setUpdateTime(date);
        purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PurchaseEntity purchase) {
        purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
