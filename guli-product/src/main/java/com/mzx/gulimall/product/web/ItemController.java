package com.mzx.gulimall.product.web;

import com.mzx.gulimall.product.service.ItemService;
import com.mzx.gulimall.product.vo.web.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author ZhenXinMa
 * @date 2020/8/18 16:50
 */
@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;


    @GetMapping(value = "/{skuId}.html")
    public String toItemPage(@PathVariable Long skuId, Model model){

        // 通过skuID查询出Item页面所需要的所有属性.
        SkuItemVo skuItemVo = itemService.item(skuId);
        model.addAttribute("item",skuItemVo);
        return "shangpinxiangqing";

    }

    @GetMapping(value = "/test/{skuId}.html")
    @ResponseBody
    public Object test(@PathVariable Long skuId){

        SkuItemVo skuItemVo = itemService.item(skuId);
        return skuItemVo;

    }

}

