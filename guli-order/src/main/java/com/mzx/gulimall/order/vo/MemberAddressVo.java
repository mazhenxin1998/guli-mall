package com.mzx.gulimall.order.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ZhenXinMa.
 * @slogan 浮生若梦, 若梦非梦, 浮生何梦? 如梦之梦.
 * @create 2020-09-16 21:53 周三.
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberAddressVo implements Serializable {

    private static final Long serialVersionUID = 1L;

    private Long id;
    /**
     * member_id
     */
    private Long memberId;
    /**
     *
     */
    private String name;
    /**
     *
     */
    private String phone;
    /**
     *
     */
    private String postCode;
    /**
     *
     */
    private String province;
    /**
     *
     */
    private String city;
    /**
     *
     */
    private String region;
    /**
     *
     */
    private String detailAddress;
    /**
     *
     */
    private String areacode;
    /**
     *
     */
    private Integer defaultStatus;

}
