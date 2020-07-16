package com.mzx.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mzx.gulimall.product.entity.CategoryEntity;
import com.mzx.gulimall.product.service.CategoryService;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.R;


/**
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 12:26:27
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 暂时不使用通过流来获取的目录.
     */
    @RequestMapping("/list/tree")
    public R list() {
        long start = System.currentTimeMillis();
        List<CategoryEntity> list = categoryService.listWithTree();
        long end = System.currentTimeMillis();
        return R.ok().put("data", list).put("time", end - start);
    }

    @RequestMapping(value = "/list/tree/sql")
    public R listSQL() {
        long start = System.currentTimeMillis();
        List<CategoryEntity> list = categoryService.listBySQL();
        long end = System.currentTimeMillis();
        return R.ok().put("data", list).put("time", end - start);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    public R info(@PathVariable("catId") Long catId) {
        CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("data", category);
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", consumes = {"application/json"})
    public R save(@RequestBody CategoryEntity category) {
        categoryService.save(category);
        return R.ok();
    }

    /**
     * 修改三级分类,上传的时候表单上传. POST.
     */
    @PostMapping(value = "/update", consumes = {"application/json"})
    public R update(@RequestBody CategoryEntity category) {

        System.out.println("修改方法进行了...");
        categoryService.updateById(category);
        return R.ok();
    }

    @PostMapping(value = "/update/sort", consumes = {"application/json"})
    public R updateSort(@RequestBody CategoryEntity[] entities) {

        System.out.println("Update Sort方法发生了");
        categoryService.updateBatchById(Arrays.asList(entities));
        return R.ok();
    }


    /**
     * 删除
     */
    @PostMapping(value = "/delete", consumes = {"application/json"})
    public R delete(@RequestBody Long[] catIds) {

        // 方法进不来.
        categoryService.removeMenus(Arrays.asList(catIds));
        return R.ok();
    }

}
