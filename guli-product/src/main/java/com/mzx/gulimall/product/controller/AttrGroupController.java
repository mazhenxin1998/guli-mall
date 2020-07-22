package com.mzx.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.mzx.gulimall.product.vo.AttrGroupWithAttrVo;
import com.mzx.gulimall.product.vo.AttrRelationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.mzx.gulimall.product.entity.AttrGroupEntity;
import com.mzx.gulimall.product.service.AttrGroupService;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.R;


/**
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 12:26:27
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    /**
     * 列表
     */
    @RequestMapping("/list/{catId}")
    public R list(@RequestParam Map<String, Object> params, @PathVariable Long catId) {

        PageUtils pageUtils = attrGroupService.queryPage(params, catId);
        return R.ok().put("page", pageUtils);

    }

    /**
     * 查询到当前分组下所有可选属性ID.
     * 1. 当前分组只能关联自己分类里面所属的属性
     * 2. 当前分组只能关联到别的分组没有引用的属性.
     *
     * @param attrgroupId
     * @return
     */
    @RequestMapping(value = "/{attrgroupId}/noattr/relation")
    public R listAttrGroup(@PathVariable Long attrgroupId, @RequestParam Map<String, Object> params) {

        System.out.println("方法发生了");
        // /product/attrgroup/{attrgroupId}/noattr/relation
        R r = attrGroupService.queryAttrDetail(params, attrgroupId);
        PageUtils pageUtils = attrGroupService.getNoRelationAttr(params, attrgroupId);
        // 要求的返回结果是从一整个AttrEntity.
        return R.ok().put("page", pageUtils);
    }

    /**
     * 查出该分类下关联的属性.
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/{attrgroupId}/attr/relation")
    public R listAttrRelation(@RequestParam Map<String, Object> params,
                              @PathVariable Long attrgroupId) {

        PageUtils pageUtils = attrGroupService.getRelationAttr(params, attrgroupId);

        return R.ok().put("data", pageUtils);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);

        return R.ok().put("attrGroup", attrGroup);
    }

    @PostMapping(value = "/attr/relation")
    public R attrRelation(@RequestBody List<AttrRelationVo> vos) {

        /*这里将属性和分组进行关联,不过这里有可能一次关联多个，所以是批量保存.*/
        return attrGroupService.saveAttrGroupRelation(vos);
    }

    /**
     * 获取分类下所有分组&关联属性.
     *
     * @param catelogId
     * @param params
     * @return
     */
    @GetMapping(value = "/{catelogId}/withattr")
    public R attrGroupWithAttr(@PathVariable Long catelogId, @RequestParam Map<String, Object> params) {

        // 获取该分类下所有的分组以及该分组所关联的对象.
        List<AttrGroupWithAttrVo> vos = attrGroupService.getGroupAndAttr(catelogId);

        return R.ok().put("data", vos);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    @RequestMapping(value = "/save/")
    public R saveDetail() {

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds) {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));
        //  这里应该进行批量删除.
        return R.ok();
    }

}
