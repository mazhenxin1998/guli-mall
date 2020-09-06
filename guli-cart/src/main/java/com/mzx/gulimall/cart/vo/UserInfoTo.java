package com.mzx.gulimall.cart.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author ZhenXinMa
 * @date 2020/9/5 18:23
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoTo {

    /**
     * 用户登录状态的下的userID,并且根据根据登录状态下的userId查询购物车的内容.
     */
    private String userId;

    /**
     * 用户未登录状态下的user-key存放在cookie中,并且用该key查询当前状态下的购物车了列表.
     */
    private String userKey;

}
