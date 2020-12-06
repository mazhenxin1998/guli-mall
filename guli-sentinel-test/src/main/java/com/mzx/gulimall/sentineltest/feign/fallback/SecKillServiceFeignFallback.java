package com.mzx.gulimall.sentineltest.feign.fallback;

import com.mzx.gulimall.sentineltest.feign.SecKillServiceFeign;
import org.springframework.stereotype.Component;

/**
 * 由于在要执行这个兜底的方法,所以必须将兜底的方法交给Spring来管理.
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-30 22:40 周五.
 */
@Component
public class SecKillServiceFeignFallback implements SecKillServiceFeign {

    @Override
    public String get() {

        return "兜底方法......";

    }

}
