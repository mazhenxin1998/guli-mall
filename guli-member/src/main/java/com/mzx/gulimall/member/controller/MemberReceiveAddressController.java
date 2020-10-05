package com.mzx.gulimall.member.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mzx.gulimall.member.entity.MemberReceiveAddressEntity;
import com.mzx.gulimall.member.service.MemberReceiveAddressService;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.R;



/**
 * 
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 17:28:33
 */
@RestController
@RequestMapping("member/memberreceiveaddress")
public class MemberReceiveAddressController {

    @Autowired
    private MemberReceiveAddressService memberReceiveAddressService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){

        PageUtils page = memberReceiveAddressService.queryPage(params);
        return R.ok().put("page", page);

    }

    /**
     * /member/memberreceiveaddress/get/{memberId}
     * @param memberId
     * @return
     */
    @GetMapping(value = "/get/{memberId}")
    public R getAddr(@PathVariable(value = "memberId") Long memberId){

        List<MemberReceiveAddressEntity> entity = memberReceiveAddressService.getAddr(memberId);
        return R.ok().put("data",entity);

    }

    @GetMapping(value = "/get/memberAddress/{id}")
    public MemberReceiveAddressEntity getById(@PathVariable(value = "id") Long id){

        return memberReceiveAddressService.getById(id);

    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		MemberReceiveAddressEntity memberReceiveAddress = memberReceiveAddressService.getById(id);

        return R.ok().put("memberReceiveAddress", memberReceiveAddress);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MemberReceiveAddressEntity memberReceiveAddress){
		memberReceiveAddressService.save(memberReceiveAddress);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MemberReceiveAddressEntity memberReceiveAddress){
		memberReceiveAddressService.updateById(memberReceiveAddress);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		memberReceiveAddressService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
