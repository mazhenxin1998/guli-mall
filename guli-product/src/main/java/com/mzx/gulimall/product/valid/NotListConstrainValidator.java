package com.mzx.gulimall.product.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 第一个泛型标注的是哪一个泛型，第二个泛型是该注解标注在那个类型的字段上.
 *
 * @author ZhenXinMa
 * @date 2020/7/15 18:00
 */
public class NotListConstrainValidator implements ConstraintValidator<NotList, Integer> {

    private List<Integer> vals = new ArrayList<>();

    /**
     * 初始化ConstraintValidator.
     * <p>
     * 参数constraintAnnotation可以获取到该注解的所有信息.
     *
     * @param constraintAnnotation
     */
    @Override
    public void initialize(NotList constraintAnnotation) {

        int[] values = constraintAnnotation.values();
        for (int value : values) {

            vals.add(value);
        }

    }

    /**
     * 自定义校验规则的方法，返回true则校验成功，返回false则返回失败。
     * @param value
     * @param context
     * @return
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {

        // value表示的是参数传进来的值.
        if (vals.contains(value)) {

            return true;
        }

        return false;
    }
}
