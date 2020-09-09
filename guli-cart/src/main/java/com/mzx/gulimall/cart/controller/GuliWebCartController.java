package com.mzx.gulimall.cart.controller;

import com.mzx.gulimall.cart.api.GuliWebCartControllerApi;
import com.mzx.gulimall.cart.constant.RedisSessionConstant;
import com.mzx.gulimall.cart.service.GuliWebCartService;
import com.mzx.gulimall.cart.vo.CartParamVo;
import com.mzx.gulimall.common.model.Cart;
import com.mzx.gulimall.common.model.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.concurrent.ExecutionException;

/**
 * 当前Controller只处理业务逻辑.
 * <p>
 * 如果仅仅实现页面之间的跳转,那么应该去<code>GuliWebJumPageController</code>,进行跳转.
 *
 * @author ZhenXinMa
 * @date 2020/9/4 7:37
 */
@Controller
public class GuliWebCartController implements GuliWebCartControllerApi {

    @Autowired
    private GuliWebCartService guliWebCartService;

    /**
     * cartList页面就是购物车的页面.
     * <p>
     * 只不过该页面分两种情况: 用户登录状态的购物车,用户没有登录状态下的购物车.
     *
     * @param request 当前请求.
     * @param model   域数据.
     * @return
     */
    @Override
    @GetMapping(value = "/cart/cartlist.html")
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
            // 使用userID来进行查询,但是如果使用user-key来查询有数据的话，那么就应该进行合并.
            // Java是值传递,但是对于对象来说,传递的是对象的地址的值.
            // 也就是说我这里将Model传进去,下面方法进行修改Model,那么修改后的值也能体现在该方法中.
            // TODO: 查询功能应该先等增加功能完成之后在进行实现.
            Cart cart = guliWebCartService.cart(request, model, false);
            model.addAttribute("cart", cart);
            return "cartList";

        }

        // 针对用户登录之后进行处理.
        Cart cart = guliWebCartService.cart(request, model, true);
        model.addAttribute("cart", cart);
        // 正常处理: 用户登录状态下查询登录状态下的列表.
        // 应该将所有的操作放到底层service进行操作,Controller中应该尽可能保持简洁的操作.
        return "cartList";
    }

    /**
     * 添加@RequestBody注解的前提是: 前台传过来的是JSON格式的数据,而不是K-V形式的.
     *
     * @param request 当前请求.
     * @param param   当前添加的商品.
     * @param model   域数据.
     * @return
     */
    @Override
    @GetMapping(value = "/add")
    public String addCart(HttpServletRequest request,
                          @Valid CartParamVo param, BindingResult bindingResult,
                          Model model) throws ExecutionException, InterruptedException {

        // 但是我们这里接受的数据,仅仅应该接受skuID和count.
        // TODO: 这里问题.
        // 目前可以接受到数据.
        // add方法目的只是为了增加商品从购物车.
        CartItem item = guliWebCartService.add(request, param, model);
        model.addAttribute("item", item);
        // TODO:暂时没有解决表单重复提交.
        // 跳转也有问题.
        return "success";

    }


    @Override
    @GetMapping(value = "/success")
    public String toCartSuccess(@RequestParam(value = "id") Long id, Model model) {

        // 返回的时候带上其Model属性.
        // Java中虽然是值传递,但是如果传递的是一个对象,那么传递的值是该对象在堆中的实际地址值.
//        CartItem item = guliWebCartService.getCartItem(id, model);
//        model.addAttribute("item", item);
        return "success";

    }

    @GetMapping(value = "/add/test")
    @ResponseBody
    public String addTest(HttpServletRequest request,
                          @Valid CartParamVo param, BindingResult bindingResult,
                          Model model) {


        return "success : " + param;

    }


}
