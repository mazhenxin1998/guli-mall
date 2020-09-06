package com.mzx.gulimall.common.model;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZhenXinMa
 * @date 2020/8/25 20:40
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberResultVo implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId
    private Long id;
    /**
     *
     */
    private Long levelId;
    /**
     *
     */
    private String username;
    /**
     *
     */
    private String password;
    /**
     *
     */
    private String nickname;
    /**
     *
     */
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

}
