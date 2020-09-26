package com.mzx.gulimall.dao;

import com.mzx.gulimall.member.SpringApplicationMemberApp;
import com.mzx.gulimall.member.dao.MemberReceiveAddressDao;
import com.mzx.gulimall.member.entity.MemberReceiveAddressEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-26 16:27 周六.
 */
@SpringBootTest(classes = SpringApplicationMemberApp.class)
@RunWith(SpringRunner.class)
public class MemberAddressDaoTest {

    @Autowired
    private MemberReceiveAddressDao memberReceiveAddressDao;

    @Test
    public void t1(){

        List<MemberReceiveAddressEntity> list = memberReceiveAddressDao.getArrrByMemberId(8L);
        list.forEach(item->{

            System.out.println(item.getDetailAddress());

        });

    }



}

