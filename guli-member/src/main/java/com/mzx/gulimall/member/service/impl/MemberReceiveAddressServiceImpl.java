package com.mzx.gulimall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.Query;
import com.mzx.gulimall.member.dao.MemberReceiveAddressDao;
import com.mzx.gulimall.member.entity.MemberReceiveAddressEntity;
import com.mzx.gulimall.member.service.MemberReceiveAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 对会员地址的操作.
 */
@Service("memberReceiveAddressService")
public class MemberReceiveAddressServiceImpl extends ServiceImpl<MemberReceiveAddressDao, MemberReceiveAddressEntity> implements MemberReceiveAddressService {

    @Autowired
    private MemberReceiveAddressDao memberReceiveAddressDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        IPage<MemberReceiveAddressEntity> page = this.page(
                new Query<MemberReceiveAddressEntity>().getPage(params),
                new QueryWrapper<MemberReceiveAddressEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<MemberReceiveAddressEntity> getAddr(Long memberId) {

        // 难道每次都要手写SQL？
        return memberReceiveAddressDao.getArrrByMemberId(memberId);

    }

}