package com.mzx.gulimall.order.feign;

import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.order.GuliMallOrderApplication;
import com.mzx.gulimall.order.vo.MemberAddressVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-26 16:31 周六.
 */
@SpringBootTest(classes = GuliMallOrderApplication.class)
@RunWith(SpringRunner.class)
public class MemberFeignTest {

    @Autowired
    private MemberServiceFeign memberServiceFeign;

    @Test
    public void t1() {

        R addr = memberServiceFeign.getAddr(8L);
        List<MemberAddressVo> list = (List<MemberAddressVo>) addr.get("data");
        System.out.println(1);

    }

    @Test
    public void t2() {

        MemberAddressVo memberAddressVo = memberServiceFeign.getById(1L);
        System.out.println(1);

    }


}
