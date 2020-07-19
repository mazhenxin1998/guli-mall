package com.mzx.gulimall.product.utils;

import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.product.exception.ResultCode;

import java.time.LocalDateTime;

/**
 * @author ZhenXinMa
 * @date 2020/7/18 15:08
 */
public class ExceptionUtils {

    public static R transformRByResultCode(ResultCode resultCode) {

        LocalDateTime time = resultCode.time();
        String message = resultCode.message();
        int code = resultCode.code();
        boolean success = resultCode.success();
        R r = new R();
        r.put("success", success);
        r.put("code", code);
        r.put("message", message);
        r.put("time", time);

        return r;
    }

}
