package com.mzx.gulimall.product.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mzx.gulimall.product.entity.CategoryBrandRelationEntity;
import com.mzx.gulimall.product.service.CategoryBrandRelationService;
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
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 列表:renren-generator原生.
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {

        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }

    @RequestMapping(value = "/catelog/list")
    public R categoryList(@RequestParam Map<String, Object> params) {

        // params 可以拿到brandId 的值.  要查询的分类和品牌关联的参数:{t=1594904589647, brandId=2}
        System.out.println("要查询的分类和品牌关联的参数:" + params.toString());
        PageUtils page = categoryBrandRelationService.queryPage(params);
        return R.ok().put("page", page).put("time", new Date());
    }

    @GetMapping(value = "/brands/list")
    public R categoryBrandList(@RequestParam Map<String, Object> params) {

        // TODO 平台属性 规格参数 新增未完善.
        return R.ok();
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存 需要将额外的分类名字和品牌名进行保存.
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {

        // 默认只接受到brandId和categoryId. 所以说我们要横向扩展.
        categoryBrandRelationService.saveDetail(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
