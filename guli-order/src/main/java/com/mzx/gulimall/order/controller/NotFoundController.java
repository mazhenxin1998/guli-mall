package com.mzx.gulimall.order.controller;

import com.mzx.gulimall.common.utils.R;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 页面找不到时发生的异常.
 *
 * @author ZhenXinMa
 * @date 2020/7/16 15:18
 */
@RestController
public class NotFoundController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @RequestMapping(value = "/error")
    public R error() {

        // 处理指定异常信息.
        return R.error(404, "您要找的页面丢失了，请稍后重试!");
    }
}
