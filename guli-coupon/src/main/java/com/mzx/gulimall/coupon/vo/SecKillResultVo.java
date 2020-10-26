package com.mzx.gulimall.coupon.vo;

import com.mzx.gulimall.coupon.entity.SmsSeckillSessionEntity;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-26 21:02 周一.
 */
@Data
@ToString
public class SecKillResultVo {

    private Page page;
    private Integer code = 0;

    @Data
    @ToString
    public static class Page<T> {

        private List<T> list;

    }

}
