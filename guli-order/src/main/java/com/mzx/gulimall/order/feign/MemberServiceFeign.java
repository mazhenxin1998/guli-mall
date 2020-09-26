package com.mzx.gulimall.order.feign;

import com.mzx.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-26 10:15 周六.
 */
@FeignClient(value = "gulimall-member")
public interface MemberServiceFeign {

    @GetMapping(value = "/member/memberreceiveaddress/get/{memberId}")
    R getAddr(@PathVariable(value = "memberId") Long memberId);


}
