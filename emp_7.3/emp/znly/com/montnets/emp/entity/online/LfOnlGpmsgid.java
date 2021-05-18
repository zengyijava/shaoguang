package com.montnets.emp.entity.online;

import java.sql.Timestamp;

public class LfOnlGpmsgid implements java.io.Serializable
{
    /**
     * @description  
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-13 上午11:10:09
     */
    private static final long serialVersionUID = 3001514446817028862L;

    private Long gmUser;
    
    private Long msgId;
    
    private Timestamp updateTime;

    public LfOnlGpmsgid()
    {
        this.updateTime = new Timestamp(System.currentTimeMillis());
    }
    public LfOnlGpmsgid(Long msgId,Long gmUser)
    {
        this.gmUser = gmUser;
        this.msgId = msgId;
        this.updateTime = new Timestamp(System.currentTimeMillis());
    }
    
  
    public Long getGmUser()
    {
        return gmUser;
    }

    public void setGmUser(Long gmUser)
    {
        this.gmUser = gmUser;
    }
    
    public Long getMsgId()
    {
        return msgId;
    }
    public void setMsgId(Long msgId)
    {
        this.msgId = msgId;
    }
    public Timestamp getUpdateTime()
    {
        return updateTime;
    }
    public void setUpdateTime(Timestamp updateTime)
    {
        this.updateTime = updateTime;
    }
    
    
}
