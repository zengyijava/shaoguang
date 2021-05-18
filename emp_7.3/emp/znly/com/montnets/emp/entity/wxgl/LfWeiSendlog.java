package com.montnets.emp.entity.wxgl;

import java.sql.Timestamp;

/**
 * 实体类： LfWeiSendlog
 * 
 * @project p_wei
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfWeiSendlog implements java.io.Serializable
{

    // 消息ID
    private Long      sendId;

    // 发送类型(0：分组群发，1：按地区发送)
    private String    tp;

    // 消息的类型(文本：text,图文：mpnews)
    private String    msgType;

    // 地区的名称(目前存的中文，以后可能需要改进)
    private String    areaValue;

    // 提交的数据
    private String    postMsg;

    // 图文模板或关联资源的ID
    private Long      tId;

    // 公众帐号
    private Long      aId;
    
    // 企业编号
    private String    corpCode;

    // 创建时间
    private Timestamp createtime;

    // 消息的状态（0：发送失败，1：已提交成功，2：发送成功）
    private Integer    status;
    
    //  微信服务器推送过来的反馈信息
    private String    eventData;

    // 提交返回的信息
    private String responseMsg;
    
    // 返回消息的id
    private String msgId;
    
    // 文本发送时，为文本内容，图文发送时，为图文标题
    private String sendContent;
    
    public Long getSendId()
    {
        return sendId;
    }

    public void setSendId(Long sendId)
    {
        this.sendId = sendId;
    }

    public String getTp()
    {
        return tp;
    }

    public void setTp(String tp)
    {
        this.tp = tp;
    }

    public String getMsgType()
    {
        return msgType;
    }

    public void setMsgType(String msgType)
    {
        this.msgType = msgType;
    }

    public String getAreaValue()
    {
        return areaValue;
    }

    public void setAreaValue(String areaValue)
    {
        this.areaValue = areaValue;
    }

    public String getPostMsg()
    {
        return postMsg;
    }

    public void setPostMsg(String postMsg)
    {
        this.postMsg = postMsg;
    }

    public Long getTId()
    {
        return tId;
    }

    public void setTId(Long tId)
    {
        this.tId = tId;
    }

    public Long getAId()
    {
        return aId;
    }

    public void setAId(Long aId)
    {
        this.aId = aId;
    }

    public String getCorpCode()
    {
        return corpCode;
    }

    public void setCorpCode(String corpCode)
    {
        this.corpCode = corpCode;
    }

    public Timestamp getCreatetime()
    {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime)
    {
        this.createtime = createtime;
    }

    /**
     * @param eventData the eventData to set
     * @author Administrator <foyoto@gmail.com>
     * @datetime 2014-5-29 下午04:14:44
     */
    public void setEventData(String eventData)
    {
        this.eventData = eventData;
    }

    /**
     * @return the eventData
     * @author Administrator <foyoto@gmail.com>
     * @datetime 2014-5-29 下午04:14:44
     */
    public String getEventData()
    {
        return eventData;
    }

    /**
     * @param status the status to set
     * @author Administrator <foyoto@gmail.com>
     * @datetime 2014-5-29 下午04:14:51
     */
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    /**
     * @return the status
     * @author Administrator <foyoto@gmail.com>
     * @datetime 2014-5-29 下午04:14:51
     */
    public Integer getStatus()
    {
        return status;
    }

    /**
     * @param msgId the msgId to set
     * @author Administrator <foyoto@gmail.com>
     * @datetime 2014-5-30 下午06:13:35
     */
    public void setMsgId(String msgId)
    {
        this.msgId = msgId;
    }

    /**
     * @return the msgId
     * @author Administrator <foyoto@gmail.com>
     * @datetime 2014-5-30 下午06:13:35
     */
    public String getMsgId()
    {
        return msgId;
    }

    /**
     * @param responseMsg the responseMsg to set
     * @author Administrator <foyoto@gmail.com>
     * @datetime 2014-5-30 下午06:13:43
     */
    public void setResponseMsg(String responseMsg)
    {
        this.responseMsg = responseMsg;
    }

    /**
     * @return the responseMsg
     * @author Administrator <foyoto@gmail.com>
     * @datetime 2014-5-30 下午06:13:43
     */
    public String getResponseMsg()
    {
        return responseMsg;
    }

    public String getSendContent()
    {
        return sendContent;
    }

    public void setSendContent(String sendContent)
    {
        this.sendContent = sendContent;
    }

    
}
