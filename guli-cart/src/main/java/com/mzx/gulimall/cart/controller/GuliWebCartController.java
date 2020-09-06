package com.mzx.gulimall.cart.controller;

import com.mzx.gulimall.cart.api.GuliWebCartControllerApi;
import com.mzx.gulimall.cart.constant.RedisSessionConstant;
import com.mzx.gulimall.cart.service.GuliWebCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 当前Controller只处理业务逻辑.
 * <p>
 * 如果仅仅实现页面之间的跳转,那么应该去<code>GuliWebJumPageController</code>,进行跳转.
 *
 * @author ZhenXinMa
 * @date 2020/9/4 7:37
 */
@Controller
@RequestMapping(value = "/cart")
public class GuliWebCartController implements GuliWebCartControllerApi {

    @Autowired
    private GuliWebCartService guliWebCartService;

    @Override
    @RequestMapping(value = "/cartlist.html")
    public String cart(HttpServletRequest request, Model model) {

        // 先判断用户是否登录,
        HttpSession session = request.getSession();
        // 如果用户状态没有登录, 那么就跳转到,
        Object user = session.getAttribute(RedisSessionConstant.PUBLIC_USER);
        if (StringUtils.isEmpty(user)) {

            // 做相应的未登录用户状态进行处理 .
            // 这个时候查询的是用户未登录状态下的列表.
            // 如果为空,那么就使用user-key来进行查询.
            // 应该从cookie中查询出来user-key.

            return "cartList";
        }

        // 正常处理: 用户登录状态下查询登录状态下的列表.


        // 应该将所有的操作放到底层service进行操作,Controller中应该尽可能保持简洁的操作.
        return "cartList";
    }
}
