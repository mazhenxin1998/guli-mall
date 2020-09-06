package com.mzx.gulimall.member.controller;

import com.mzx.gulimall.common.model.MemberResultVo;
import com.mzx.gulimall.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhenXinMa
 * @date 2020/8/25 20:53
 */
@RestController
public class MemberControllerFeign {


    @Autowired
    private MemberService memberService;

    @RequestMapping(value = "/member/get")
    public MemberResultVo getMemberVo(@RequestParam(value = "phone") String phone){

        MemberResultVo resultVo = memberService.getPassword(phone);
        System.out.println("resultVo");
        return resultVo;
    }


}
