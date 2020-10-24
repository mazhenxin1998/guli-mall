package com.mzx.gulimall.feign;

import com.alipay.api.domain.AntOcrVehiclelicenseIdentifyModel;
import com.alipay.api.domain.PublicAuditStatus;
import com.mzx.gulimall.pay.GuliMallPaymentApplication;
import com.mzx.gulimall.pay.feign.WareServiceFeign;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-24 16:34 周六.
 */
@SpringBootTest(classes = GuliMallPaymentApplication.class)
@RunWith(SpringRunner.class)
public class WareServiceFeignTest {

    @Autowired
    private WareServiceFeign wareServiceFeign;

    @Test
    public void t1(){

        wareServiceFeign.updateSkuWare("202010241553059541319909767447109634");

    }

}
