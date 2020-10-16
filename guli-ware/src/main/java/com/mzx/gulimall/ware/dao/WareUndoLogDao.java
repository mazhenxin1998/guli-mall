package com.mzx.gulimall.ware.dao;

import com.mzx.gulimall.ware.entity.WareUndoLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-16 16:29 周五.
 */
@Mapper
@Repository
public interface WareUndoLogDao {

    /**
     * 打向日志库消息.
     *
     * @param undoLogEntity
     */
    void rollBackLog(@Param("undoLogEntity") WareUndoLogEntity undoLogEntity);

}
