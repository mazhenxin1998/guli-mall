package com.mzx.gulimall.oauth.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author ZhenXinMa
 * @date 2020/8/25 19:47
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginVo {

    @NotNull
    private String phone;

    @NotEmpty
    private String password;


    private String origin_url;


}
