package com.mzx.gulimall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzx.gulimall.common.model.MemberResultVo;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.Query;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.member.dao.MemberDao;
import com.mzx.gulimall.member.entity.MemberEntity;
import com.mzx.gulimall.member.service.MemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public R regSave(MemberEntity member) {

        System.out.println("regSave方法执行了...");
        /*
         * --------------------------------------------------------
         * 传进来的数据只有三个数据项: 用户名、密码、密码.
         * level_id默认为1: 黄金会员.
         * --------------------------------------------------------
         * */
        // 默认为普通会员.
        member.setLevelId(1L);
        member.setCreateTime(new Date());
        // 应该将密码进行加密.
        // 使用Spring提供的随机盐值进行加密.
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 加密之后的密码.
        String encodedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encodedPassword);
        // 密码匹配.
//        boolean b = passwordEncoder.matches("带验证的密码", "加密之后的密码");

        try {

            baseMapper.insert(member);
            // 再次调用member将会发现其属性ID对应着表中该行的ID.
            if (member.getId() == null) {

                return R.error(20003, "主键回填失败,获取不到新增加的用户ID.");
            } else {

                return R.ok().put("userID", member.getId());
            }
        } catch (Exception e) {

            return R.error();
        }

    }

    @Override
    public MemberResultVo getPassword(String phone) {

        MemberEntity memberEntity = baseMapper.selectOne(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        MemberResultVo resultVo = new MemberResultVo();
        BeanUtils.copyProperties(memberEntity,resultVo);
        return resultVo;
    }

}