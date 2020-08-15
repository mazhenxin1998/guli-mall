package com.mzx.gulimall.search.web;

import com.mzx.gulimall.search.service.IGuliMallSearchService;
import com.mzx.gulimall.search.vo.SearchParam;
import com.mzx.gulimall.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author ZhenXinMa
 * @date 2020/8/12 21:16
 */
@Controller
public class WebSearchController {

    @Autowired
    private IGuliMallSearchService iGuliMallSearchService;

    @GetMapping(value = "/search/list.html")
    public String list(SearchParam searchParam, Model model){

        SearchResult result = iGuliMallSearchService.search(searchParam);
        model.addAttribute("result",result);
        return "list";
    }

    @GetMapping(value = "/list.html")
    public String toListHtml(){

        return "list";
    }

    @PostMapping(value = "/search/test")
    @ResponseBody
    public SearchResult t3(SearchParam searchParam){

        SearchResult result = iGuliMallSearchService.search(searchParam);
        return result;
    }

}
