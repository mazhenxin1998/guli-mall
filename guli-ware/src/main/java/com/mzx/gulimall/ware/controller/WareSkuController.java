package com.mzx.gulimall.ware.controller;

import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.ware.entity.WareSkuEntity;
import com.mzx.gulimall.ware.service.WareSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 商品库存
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-25 14:15:39
 */
@RestController
@RequestMapping("ware/waresku")
public class WareSkuController {

    @Autowired
    private WareSkuService wareSkuService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {

        PageUtils page = wareSkuService.queryPageDetails(params);
        return R.ok().put("page", page);
    }


    /**
     * 报错找不到List初始化函数.
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/get/list")
    public R getListStock(@RequestBody String[] ids) {

        // 批量查询ids对应的库存数量.
        // data属性可能为空值.
        // 现在需要将ids转换为List.
        System.out.println("ids的值是: " + ids);
        List<String> list = Arrays.asList(ids);
        List<Long> listIds = list.stream().map(item -> {

            return Long.valueOf(item);

        }).collect(Collectors.toList());
        return wareSkuService.getListStock(listIds);

    }

    @GetMapping(value = "/get/{skuId}")
    public boolean getStock(@PathVariable Long skuId) {

        return wareSkuService.getStock(skuId);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WareSkuEntity wareSku) {
        wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WareSkuEntity wareSku) {
        wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
