package com.mzx.gulimall.seckill.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-12-01 16:02 周二.
 */
@Data
@ToString
public class ResultVo<T> {

    /**
     * 存数组类型的数据.
     */
    List<T> list;

    /**
     * 存对象数据类型.
     */
    T data;

    Integer code;

}
