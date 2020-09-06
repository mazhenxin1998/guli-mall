package com.mzx.gulimall.oauth.fiegn;

import com.mzx.gulimall.common.model.MemberResultVo;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.oauth.entity.MemberEntity;
import com.mzx.gulimall.oauth.vo.MemberVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping("/member/member/info/{id}")
    R info(@PathVariable("id") Long id);

    /**
     * 通过用户的ID查询出该用户的所有信息.
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/member/member/get/info/{id}")
    MemberEntity getMemberInfo(@PathVariable(value = "id") Long id);

    /**
     * 远程调用会员服务通过手机号查询出该手机号所对应的密码 返回值是一个String类型的字符串.
     *
     * @param phone
     * @return
     */
    @RequestMapping(value = "/member/get")
    MemberResultVo getPasswordByPhone(@RequestParam(value = "phone") String phone);

}
