package com.mzx.gulimall.product.web;

import com.mzx.gulimall.product.entity.CategoryEntity;
import com.mzx.gulimall.product.service.CategoryService;
import com.mzx.gulimall.product.vo.web.Catalog2Vo;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author ZhenXinMa
 * @date 2020/8/18 17:12
 */
@Controller
public class WebController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = {"/", "index.html", "index"})
    public String index(Model model) {

        /*
         * --------------------------------------------------------
         * 需要将所有的一级分类放到request域中.
         * --------------------------------------------------------
         * */
        List<CategoryEntity> categoryEntityList = categoryService.findOneCategoryList();
        model.addAttribute("categorys", categoryEntityList);
        return "index";
    }

    @GetMapping(value = "/web/index/json/catalog.json")
    @ResponseBody
    public Map<String, List<Catalog2Vo>> getCatalog() {

        /*
         * --------------------------------------------------------
         * 返回值以一级分类的ID作为Key，其子分类作为其Key的值.
         * --------------------------------------------------------
         * */
        return categoryService.findAllCatagory();
    }


}
