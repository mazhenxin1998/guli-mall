package com.mzx.gulimall.product.web;

import com.mzx.gulimall.product.entity.CategoryEntity;
import com.mzx.gulimall.product.service.CategoryService;
import com.mzx.gulimall.product.service.LoginService;
import com.mzx.gulimall.product.vo.web.Catalog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    private LoginService loginService;

    @GetMapping(value = {"/", "index.html", "index"})
    public String index(HttpServletResponse response,
                        HttpServletRequest request,
                        Model model) {

        List<CategoryEntity> categoryEntityList = categoryService.findOneCategoryList();
        model.addAttribute("categorys", categoryEntityList);
        // 不管用户是否进行登录, 这里应该给每个用户分配一个默认的user-key
        // 并且放入cookie中.
        // 添加的时候应该做下判断.
        loginService.addUserKey(request,response);
        return "index";

    }

    @GetMapping(value = "/web/index/json/catalog.json")
    @ResponseBody
    public Map<String, List<Catalog2Vo>> getCatalog() {

        return categoryService.findAllCatagory();
    }

    @GetMapping(value = "/userlist.html")
    public String regSuccessToIndexPage(Model model) {

        return "index";
    }


}
