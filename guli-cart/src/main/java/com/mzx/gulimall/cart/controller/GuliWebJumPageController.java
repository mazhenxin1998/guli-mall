package com.mzx.gulimall.cart.controller;

import com.mzx.gulimall.cart.api.GuliWebJumpPageControllerApi;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 该Controller仅仅实现静态跳转.
 * <p>
 * 如果要对业务做一些实际的操作,应该去GuliWebCartController控制器中进行业务的实际操作,当前Controller仅仅支持无状态的页面之间的跳转.
 *
 * @author ZhenXinMa
 * @date 2020/9/5 16:43
 */
@Controller
@RequestMapping(value = "/cart")
public class GuliWebJumPageController implements GuliWebJumpPageControllerApi {


}
