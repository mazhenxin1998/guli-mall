package com.mzx.gulimall.ware.service.impl;

import com.alibaba.fastjson.JSON;
import com.mzx.gulimall.ware.dao.WareUndoLogDao;
import com.mzx.gulimall.ware.entity.WareUndoLogEntity;
import com.mzx.gulimall.ware.service.WareUndoLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-16 16:32 周五.
 */
@Service
public class WareUndoLogServiceImpl implements WareUndoLogService {

    @Autowired
    private WareUndoLogDao wareUndoLogDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rollBackLog(String messageId) {

        WareUndoLogEntity undoLogEntity = new WareUndoLogEntity();
        undoLogEntity.setOrderSn(messageId);
        undoLogEntity.setMessageId(messageId);
        undoLogEntity.setRollbackInfo("库存服务向MQ中发送消息,MQ BROKER没有接受到消息.");
        undoLogEntity.setLogCreated(new Date());
        undoLogEntity.setLogModified(new Date());
        String contentStringJson = JSON.toJSONString(undoLogEntity);
        undoLogEntity.setContext(contentStringJson);
        wareUndoLogDao.rollBackLog(undoLogEntity);
        System.out.println("消息 " + messageId + " 打向日志库成功.");

    }

}
