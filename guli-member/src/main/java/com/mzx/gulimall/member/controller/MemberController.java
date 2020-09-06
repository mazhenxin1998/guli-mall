package com.mzx.gulimall.member.controller;

import com.mzx.gulimall.common.model.MemberResultVo;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.member.entity.MemberEntity;
import com.mzx.gulimall.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 17:28:33
 */
@RestController
@RequestMapping("member/member")
public class MemberController {


    @Autowired
    private MemberService memberService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    @GetMapping(value = "/get/info/{id}")
    public MemberEntity getMemberInfo(@PathVariable Long id){

        return memberService.getById(id);
    }

    @GetMapping(value = "/get/password")
    public MemberResultVo getPasswordByPhone(@RequestParam(value = "phone") String phone){

        System.out.println("111 phone");
        return memberService.getPassword(phone);
    }

    /**
     * 增加会员成功,则返回状态码为0.
     *
     * @param member
     * @return
     */
    @PostMapping("/add")
    public R save(@RequestBody MemberEntity member) {

        return memberService.regSave(member);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MemberEntity member) {
        memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
