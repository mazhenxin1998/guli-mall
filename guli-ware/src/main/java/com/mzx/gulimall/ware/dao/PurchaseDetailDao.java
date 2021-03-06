package com.mzx.gulimall.ware.dao;

import com.mzx.gulimall.ware.entity.PurchaseDetailEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 
 * 
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-25 14:15:39
 */
@Mapper
@Repository
public interface PurchaseDetailDao extends BaseMapper<PurchaseDetailEntity> {
	
}
