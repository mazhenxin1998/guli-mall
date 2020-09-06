package com.mzx.gulimall.cart.controller;

import com.mzx.gulimall.cart.api.GuliWebJumpPageControllerApi;
import com.mzx.gulimall.cart.service.GuliWebCartService;
import com.mzx.gulimall.cart.vo.CartParamVo;
import com.mzx.gulimall.common.model.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.concurrent.ExecutionException;

/**
 * 该Controller仅仅实现静态跳转.
 * <p>
 * 如果要对业务做一些实际的操作,应该去GuliWebCartController控制器中进行业务的实际操作,当前Controller仅仅支持无状态的页面之间的跳转.
 *
 * @author ZhenXinMa
 * @date 2020/9/5 16:43
 */
@Controller
public class GuliWebJumPageController implements GuliWebJumpPageControllerApi {


    @Autowired
    private GuliWebCartService guliWebCartService;

    @GetMapping(value = "/to/success")
    public String toSuccess(@Valid CartParamVo param,
                            HttpServletRequest request,
                            Model model) {

        System.out.println(param.toString());
        // 添加了下面的代码之后将会出现错误.
        CartItem item = guliWebCartService.add(request, param, model);
        model.addAttribute("item", item);

        return "success";
    }



}
