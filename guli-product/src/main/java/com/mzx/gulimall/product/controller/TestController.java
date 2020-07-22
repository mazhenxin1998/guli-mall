package com.mzx.gulimall.product.controller;

import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.product.dao.CategoryDao;
import com.mzx.gulimall.product.entity.BrandEntity;
import com.mzx.gulimall.product.entity.CategoryEntity;
import com.mzx.gulimall.product.entity.SmsMemberPriceEntity;
import com.mzx.gulimall.product.entity.SmsSpuBoundsEntity;
import com.mzx.gulimall.product.feign.ICouponServiceFeign;
import com.mzx.gulimall.product.valid.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZhenXinMa
 * @date 2020/7/10 16:36
 */
@RestController
public class TestController {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private ICouponServiceFeign iCouponServiceFeign;


    @RequestMapping(value = "/")
    public String t1() {
        return "renren-genrator is SUCCESS";
    }

    @RequestMapping(value = "/test/list/category")
    public Object t2() {

        List<CategoryEntity> entities = categoryDao.get();
        return entities;
    }

    /**
     * 注解: @Validated表示开启分组校验. 一旦开启分组校验，没有添加分组校验的字段将不会起作用.
     * 也就是说，一旦开起了分组校验，那么就必须将所有要校验的字段进行分组.
     *
     * @param brandEntity
     * @param result
     * @return
     */
    @PostMapping(value = "/validation/notnull")
    public R t3(@Validated(value = {Select.class}) @RequestBody BrandEntity brandEntity, BindingResult result) {

        // 如果添加@Validated(value = {Select.class}) 则表示只针对属于Select那一组的校验注解进行校验.
        // 目前来说，Entity里面只有name属性添加了Select分组，那么该情况下只针对name属性进行校验.
        if (result.hasErrors()) {

            // 这里就说明这里有校验异常发生了，
            Map<String, String> data = new HashMap<>();
            // 拿到所有出错的列
            List<FieldError> errorList = result.getFieldErrors();
            errorList.forEach(item -> {
                data.put(item.getField(), item.getDefaultMessage());
            });

            return R.error(400, "JSR303参数校验失败").put("data", data);
        } else {

            return R.ok();
        }

    }

    @PostMapping(value = "/validation/t")
    public R t4(@Validated(value = {Select.class}) @RequestBody BrandEntity brandEntity) {

        // 如果这个方法进行参数校验出现错误.
        return R.ok();
    }

    @PostMapping(value = "/test/fegin")
    public void testFeign() {

        SmsSpuBoundsEntity entity = new SmsSpuBoundsEntity();
        entity.setBuyBounds(new BigDecimal(10));
        entity.setGrowBounds(new BigDecimal(20));
        entity.setId(1111L);
        entity.setSpuId(1111L);
        entity.setWork(5);
        iCouponServiceFeign.save(entity);

    }

    @RequestMapping(value = "/test/coupon/member")
    public void testFeignCouponMember() {

        List<SmsMemberPriceEntity> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {

            SmsMemberPriceEntity entity = new SmsMemberPriceEntity();
            entity.setAddOther(10);
            entity.setMemberLevelId(10L);
            entity.setMemberLevelName("XXX");
            entity.setMemberPrice(new BigDecimal(10));
            entity.setSkuId(10L);
            list.add(entity);
        }

        iCouponServiceFeign.saveMemberPriceBatch(list);

    }

}
