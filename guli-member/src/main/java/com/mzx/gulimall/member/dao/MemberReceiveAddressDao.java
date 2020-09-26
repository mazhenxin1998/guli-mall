package com.mzx.gulimall.member.dao;

import com.mzx.gulimall.member.entity.MemberReceiveAddressEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * 
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 17:28:33
 */
@Mapper
@Repository
public interface MemberReceiveAddressDao extends BaseMapper<MemberReceiveAddressEntity> {

    /**
     * 根据会员的ID查询当前会员对应的地址.
     * @param memberId
     * @return
     */
    List<MemberReceiveAddressEntity> getArrrByMemberId(@Param(value = "memberId") Long memberId);
}
