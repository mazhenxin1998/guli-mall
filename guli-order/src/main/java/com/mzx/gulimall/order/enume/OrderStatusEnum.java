package com.mzx.gulimall.order.enume;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-04 19:33 周日.
 */
public enum OrderStatusEnum {

    CREATE_NEW(0,"待付款"),
    PAYED(1,"以付款"),
    SENDED(2,"已发货"),
    RECIEVED(3,"已完成"),
    CANCLED(4,"已取消"),
    SERVICING(5,"售后中"),
    SERVICED(6,"售后完成");

    private Integer code;
    private String message;

    OrderStatusEnum(Integer code, String message) {

        this.code = code;
        this.message = message;

    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
