package com.mzx.gulimall.product.controller;

import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.product.entity.SpuInfoEntity;
import com.mzx.gulimall.product.service.SpuInfoService;
import com.mzx.gulimall.product.vo.SpuSaveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * spu
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 12:26:27
 */
@RestController
@RequestMapping("product/spuinfo")
public class SpuInfoController {

    @Autowired
    private SpuInfoService spuInfoService;

    /**
     * 根据给定的条件进行模糊查询和精确查询.
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {

        PageUtils page = spuInfoService.queryPageDetails(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return R.ok().put("spuInfo", spuInfo);
    }

    /**
     * 根据上传进来的SPU的ID进行商品发布.
     *
     * @param spuId
     * @return
     */
    @PostMapping(value = "/{spuId}/up")
    public R up(@PathVariable Long spuId) {

        System.out.println("要上架的商品的ID: " + spuId);
        spuInfoService.up(spuId);

        return R.ok();
    }

    /**
     * SPU新增.
     */
    @RequestMapping("/save")
    public R save(@RequestBody SpuSaveVo vo) {

        return spuInfoService.saveSpuDetails(vo);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SpuInfoEntity spuInfo) {
        spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
