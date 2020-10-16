package com.mzx.gulimall.ware.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-16 16:24 周五.
 */
public class WareUndoLogEntity implements Serializable {

    /***
     * 订单打回日志的回滚单ID.
     */
    Long id;
    /**
     * 回滚消息的唯一ID.
     */
    String messageId;
    /**
     * 该回滚消息关联的订单的ID.
     */
    String orderSn;

    /**
     * 回滚消息的消息体.
     * JSON格式.
     */
    String context;
    /**
     * 回滚消息的原因.
     */
    String rollbackInfo;
    /**
     * 回滚日志的状态.
     */
    Integer logStatus;
    /**
     * 回滚日志的创建时间.
     */
    Date logCreated;
    /**
     * 回滚日志的修改时间.
     */
    Date logModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getRollbackInfo() {
        return rollbackInfo;
    }

    public void setRollbackInfo(String rollbackInfo) {
        this.rollbackInfo = rollbackInfo;
    }

    public Integer getLogStatus() {
        return logStatus;
    }

    public void setLogStatus(Integer logStatus) {
        this.logStatus = logStatus;
    }

    public Date getLogCreated() {
        return logCreated;
    }

    public void setLogCreated(Date logCreated) {
        this.logCreated = logCreated;
    }

    public Date getLogModified() {
        return logModified;
    }

    public void setLogModified(Date logModified) {
        this.logModified = logModified;
    }

    @Override
    public String toString() {
        return "WareUndoLogEntity{" +
                "id=" + id +
                ", messageId='" + messageId + '\'' +
                ", context='" + context + '\'' +
                ", rollbackInfo='" + rollbackInfo + '\'' +
                ", logStatus=" + logStatus +
                ", logCreated=" + logCreated +
                ", logModified=" + logModified +
                '}';
    }
}
