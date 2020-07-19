package com.mzx.gulimall.product.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZhenXinMa
 * @date 2020/7/18 14:47
 */
public class CustomException extends RuntimeException {

    private ResultCode resultCode;

    public CustomException(ResultCode resultCode) {
        super("错误信息: "+resultCode.message());
        // 传进来一个错误信息.
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

}
