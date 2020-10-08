package com.mzx.gulimall.order.dao;

import com.mzx.gulimall.order.entity.Test;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-07 15:38 周三.
 */
@Mapper
@Repository
public interface TestDao {

    /**
     * 模拟增加一个数据.
     * @param test
     */
    void insert(@Param("test") Test test);

}
