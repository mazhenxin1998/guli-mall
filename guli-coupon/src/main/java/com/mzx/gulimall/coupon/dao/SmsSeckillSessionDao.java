package com.mzx.gulimall.coupon.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzx.gulimall.coupon.entity.SmsSeckillSessionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 17:45:21
 */
@Mapper
@Repository
public interface SmsSeckillSessionDao extends BaseMapper<SmsSeckillSessionEntity> {

    /**
     * 分页查询.
     *
     * @param page
     * @param size
     * @return
     */
    List<SmsSeckillSessionEntity> listSeckillSessions(@Param("page") Integer page, @Param("size") Integer size);
}
