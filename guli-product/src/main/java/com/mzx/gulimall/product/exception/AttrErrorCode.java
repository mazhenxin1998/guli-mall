package com.mzx.gulimall.product.exception;

/**
 * @author ZhenXinMa
 * @date 2020/7/18 15:02
 */
public enum AttrErrorCode implements ResultCode {
    // attrgroupId
    ATTR_GROUP_ID_IS_NULL(false, 20001, "上传的参数属性分组ID为空!,请重新上传.");

    boolean success;

    int code;

    String message;

    AttrErrorCode(boolean success, int code, String message) {

        this.success = success;
        this.code = code;
        this.message = message;
    }


    @Override
    public boolean success() {
        return false;
    }

    @Override
    public int code() {
        return 0;
    }

    @Override
    public String message() {
        return null;
    }
}
