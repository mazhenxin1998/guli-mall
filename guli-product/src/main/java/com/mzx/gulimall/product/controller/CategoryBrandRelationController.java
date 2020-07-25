package com.mzx.gulimall.product.controller;

import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.product.entity.CategoryBrandRelationEntity;
import com.mzx.gulimall.product.service.CategoryBrandRelationService;
import com.mzx.gulimall.product.vo.CategoryBrandRelationVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


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

    /**
     * 获取当前分类下的所有品牌. 前提是当前分类是第三级分类.
     *
     * @param params
     * @return
     */
    @GetMapping(value = "/brands/list")
    public R categoryBrandList(@RequestParam Map<String, Object> params) {

        /*
         * --------------------------------------------------------
         * 在接受前台传过来的数据的时候,一定要注意String和其他类型的转换，
         * 每次转换的时候都用类型的parse方法进行类型转换.
         * --------------------------------------------------------
         * */
        
        // TODO 平台属性 规格参数 新增未完善.
        if (params.size() == 1) {

            return R.ok().put("data", null);
        }
        
        Object catId = params.get("catId");
        long l = 0;
        if (!StringUtils.isEmpty(catId.toString())) {

            l = Long.parseLong(catId.toString());
        }
        List<CategoryBrandRelationVo> vos = categoryBrandRelationService.getBrandsByCategoryId(l);
        // 返回的data应该是一个CategoryBrandRelationVo的一个数组.
        return R.ok().put("data", vos);
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
