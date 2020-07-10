package com.mzx.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.order.entity.OrderOperateHistoryEntity;

import java.util.Map;

/**
 * 订单操作历史记录
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 16:44:36
 */
public interface OrderOperateHistoryService extends IService<OrderOperateHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

