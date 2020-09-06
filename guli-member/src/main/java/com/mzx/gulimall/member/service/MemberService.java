package com.mzx.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mzx.gulimall.common.model.MemberResultVo;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.member.entity.MemberEntity;

import java.util.Map;

/**
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 17:28:33
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 增加普通会员.
     *
     * @param member
     * @return
     */
    R regSave(MemberEntity member);

    /**
     * 通过手机号查询出该手机号所对应加密后的密码.
     *
     * @param phone
     * @return
     */
    MemberResultVo getPassword(String phone);
}

