package com.mzx.gulimall.order.exception;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-07 15:47 周三.
 */
public class TransactionalException extends RuntimeException {

    private String msg;

    public TransactionalException(String message) {

        super(message);
        this.msg = message;

    }

}
