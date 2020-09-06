package com.mzx.gulimall.oauth.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * @author ZhenXinMa
 * @date 2020/8/21 22:28
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberVo implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * id
     */
    private Long id;
    /**
     *
     */
    private Long levelId;
    /**
     *
     */
    @NotEmpty(message = "用户名不能为空. ")
    private String username;
    /**
     *
     */
    @NotEmpty(message = "密码不能为空.")
    private String password;
    /**
     *
     */
    private String nickname;
    /**
     *
     */
    @NotEmpty(message = "手机不能为空.")
    private String mobile;
    /**
     *
     */
    private String email;
    /**
     * ͷ
     */
    private String header;
    /**
     *
     */
    private Integer gender;
    /**
     *
     */
    private Date birth;
    /**
     *
     */
    private String city;
    /**
     * ְҵ
     */
    private String job;
    /**
     *
     */
    private String sign;
    /**
     *
     */
    private Integer sourceType;
    /**
     *
     */
    private Integer integration;
    /**
     *
     */
    private Integer growth;
    /**
     *
     */
    private Integer status;
    /**
     * ע
     */
    private Date createTime;
    /**
     * 上传进来的验证码.
     */
    private String oauthCode;

    public MemberVo(String mobile,String username){

        this.mobile = mobile;
        this.username = username;
    }


}
