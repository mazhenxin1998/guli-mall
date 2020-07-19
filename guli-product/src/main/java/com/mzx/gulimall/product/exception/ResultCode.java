package com.mzx.gulimall.product.exception;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 自定义异常.
 *
 * @author ZhenXinMa
 * @date 2020/7/18 14:57
 */
public interface ResultCode {

    boolean success();

    int code();

    String message();

    /**
     * 每一个实现了该接口的类都有一个默认的time时间戳.
     *
     * @return
     */
    default LocalDateTime time() {
        return LocalDateTime.now();
    }

    ;


}
