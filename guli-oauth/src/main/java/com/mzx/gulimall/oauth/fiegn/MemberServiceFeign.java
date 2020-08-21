package com.mzx.gulimall.oauth.fiegn;

import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.oauth.vo.MemberVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ZhenXinMa
 * @date 2020/8/21 22:37
 */
@FeignClient(value = "gulimall-member")
public interface MemberServiceFeign {

    /**
     * MemberVo和Entity里面的字段完全一样.
     *
     * @param member
     * @return
     */
    @PostMapping("/member/member/add")
    R regUser(@RequestBody MemberVo member);


}
