package com.mzx.gulimall.ware.service;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-16 16:32 周五.
 */
public interface WareUndoLogService {

    /**
     * 消息打向日志库.
     *
     * @param messageId 打向日志库的消息ID.
     */
    void rollBackLog(String messageId);

}
