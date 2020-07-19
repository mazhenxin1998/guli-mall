package com.mzx.gulimall.product.controller;

import java.util.Arrays;
import java.util.Map;

import com.mzx.gulimall.product.vo.AttrVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mzx.gulimall.product.entity.AttrEntity;
import com.mzx.gulimall.product.service.AttrService;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.R;


/**
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 12:26:27
 */
@RestController
@RequestMapping("product/attr")
@Api(value = "商品属性后台接口")
public class AttrController {

    @Autowired
    private AttrService attrService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * baseList：是规格参数查询.
     *
     * @param catelogId
     * @param params
     * @return
     */
    @RequestMapping(value = "/base/list/{catelogId}")
    public R listBase(@PathVariable Long catelogId, @RequestParam Map<String, Object> params) {

        // TODO  属性查询处理.
        // 重新进行查询
        System.out.println("base/list方法发生 其携带的ID: " + catelogId);
        PageUtils pageUtils = attrService.queryPage(params, catelogId);
        return R.ok().put("page", pageUtils);
    }

    /**
     * 销售属性的查询.
     *
     * @param id
     * @param params
     * @return
     */
    @GetMapping(value = "/sale/list/{id}")
    public R listSale(@PathVariable Long id, @RequestParam Map<String, Object> params) {

        // 查询attr_type 值为0的集合.
//        PageUtils page = attrService.queryPage(params);
        PageUtils pageUtils =  attrService.queryPageDetail(params,id);
        return R.ok().put("page", pageUtils);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId) {
        AttrEntity attr = attrService.getById(attrId);

        return R.ok().put("attr", attr);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrVo attr) {

        // 逆向工程生成的保存
//        attrService.save(attr);
        // 自定义保存.
        // 新增了一个属性， 但是还需要在其与对应的属性组进行关联.
        attrService.saveDetail(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrEntity attr) {
        attrService.updateById(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds) {

        // 删除的时候需要将两张表中的属性都删除.
        // 批量删除和删除默认都是一个方法.
        attrService.removeByIds(Arrays.asList(attrIds));
//        PageUtils pageUtils = attrService.deleteBatchByIds(attrIds);

        return R.ok();
    }

}
