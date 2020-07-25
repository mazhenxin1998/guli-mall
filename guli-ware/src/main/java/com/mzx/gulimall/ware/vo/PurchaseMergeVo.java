package com.mzx.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author ZhenXinMa
 * @date 2020/7/25 16:24
 */
@Data
public class PurchaseMergeVo {


    private Long purchaseId;
    private List<Long> items;


}
