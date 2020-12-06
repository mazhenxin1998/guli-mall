package com.mzx.gulimall.product.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Product服务定义的返回类型，用于远程返回数据.
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-12-05 17:06 周六.
 */
@Data
@ToString
public class ProductResultVo<T> {

    /**
     * 本次返回的状态码.
     */
    private Integer code;

    /**
     * 用于封装要返回数据的信息.
     */
    private List<T> list;

    /**
     * 用于返回不是集合的数据.
     */
    private T data;

}
