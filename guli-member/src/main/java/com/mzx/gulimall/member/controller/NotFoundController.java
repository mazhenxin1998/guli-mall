package com.mzx.gulimall.member.controller;

import com.mzx.gulimall.common.utils.R;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author ZhenXinMa
 * @date 2020/7/16 14:39
 */
@RestController
public class NotFoundController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    /**
     * 待说明.
     * @return
     */
    @RequestMapping(value = ERROR_PATH)
    public R error(){

        return R.error(404,"您要查找的页面不存!").put("timestaup",new Date());
    }
}
