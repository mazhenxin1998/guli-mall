package com.mzx.gulimall.product.controller;

import java.util.Arrays;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mzx.gulimall.product.entity.BrandEntity;
import com.mzx.gulimall.product.service.BrandService;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.R;


/**
 * Ʒ
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 12:26:27
 */
@RestController
@RequestMapping("product/brand")
@Api(value = "商品品牌接口")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value = "商品品牌查询接口")
    @ApiImplicitParam(name = "params", value = "要查询的条件", defaultValue = "Null", required = false, dataType = "Map")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    @ApiOperation(value = "商品品牌查询接口根据商品品牌ID")
    @ApiImplicitParam(name = "brandId", value = "要查询的品牌的品牌ID", required = true, dataType = "Long")
    public R info(@PathVariable("brandId") Long brandId) {


        BrandEntity brand = brandService.getById(brandId);
        return R.ok().put("brand", brand);
    }


    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value = "商品品牌保存接口")
    public R save(@RequestBody BrandEntity brand) {
        brandService.save(brand);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @ApiOperation(value = "品牌内容更新接口")
    public R update(@RequestBody BrandEntity brand) {
        brandService.updateById(brand);

        return R.ok();
    }

    @PostMapping(value = "/update/status")
    @ApiOperation(value = "更新品牌显示状态的接口")
    public R updateStatus(@RequestBody BrandEntity brandEntity) {

        brandService.updateById(brandEntity);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value = "根据品牌ID删除该品牌")
    @ApiImplicitParam(name = "brandIds", value = "要删除的品牌的ID", dataType = "Long[]", required = true)
    public R delete(@RequestBody Long[] brandIds) {

        brandService.removeByIds(Arrays.asList(brandIds));
        return R.ok();
    }

}
