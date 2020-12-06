package com.mzx.gulimall.sentineltest.exception;

import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 注意在该包下发生的异常,统一交给这里来处理.
 * <p>
 * 先设置成全局异常不起作用.
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-30 20:05 周五.
 */
public class ControllerException {

    /**
     * Blocked by Sentinel (flow limiting) 限流异常.
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = FlowException.class)
    public String sentinel(FlowException e) {

        e.printStackTrace();
        return "Sentinel 异常...";

    }


    /**
     * 为什么会出现全局异常.?
     *
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public String e(Exception e) {

        e.printStackTrace();
        return "全局异常. ";

    }

}
