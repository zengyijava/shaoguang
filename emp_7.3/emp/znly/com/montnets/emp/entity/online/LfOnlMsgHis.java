package com.montnets.emp.entity.online;

import java.sql.Timestamp;


/**
 * 实体类： LfOnlMsgHis
 *
 * @project p_onl
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfOnlMsgHis implements java.io.Serializable
{
   
 /**
     * @description  
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-16 上午10:12:55
     */
    private static final long serialVersionUID = -8158441809896729029L;

    // 自增ID
    private Long MId;
   
    // 关联的公众账号id
    private Long AId;
   
    // 发送者
    private String   fromUser;
   
    // 接收者
    private String   toUser;
   
    // 服务号
    private String   serverNum;
   
    // 内容
    private String   message;
   
    // 发送时间
    private Timestamp    sendTime;
   
    // // 消息类型text-文本，voice-声音，image-单图
    private String  msgType;
   
    // 推送类型1-手机to客服，2-客服to手机，3-客服to客服，4群组,5，转接客服，6-APPto客服，7-客服toAPP
    private Integer  pushType;
   
    /**
     * 默认构造函数
     * @description           			 
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-23 下午02:03:32
     */
	public LfOnlMsgHis()
    {
        super();
    }

    public LfOnlMsgHis( Long aId, String fromUser, String toUser, String serverNum, String message, Timestamp sendTime, String msgType, int pushType)
    {
        AId = aId;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.serverNum = serverNum;
        this.message = message;
        this.sendTime = sendTime;
        this.msgType = msgType;
        this.pushType = pushType;
    }

    public Long getMId()
    {
        return MId;
    }

    public void setMId(Long mId)
    {
        MId = mId;
    }

    public Long getAId()
    {
        return AId;
    }

    public void setAId(Long aId)
    {
        AId = aId;
    }

    public String getFromUser()
	{
		return fromUser;
	}

	public void setFromUser(String fromUser)
	{
		this.fromUser = fromUser;
	}
    
	public String getToUser()
	{
		return toUser;
	}

	public void setToUser(String toUser)
	{
		this.toUser = toUser;
	}
    
	public String getServerNum()
	{
		return serverNum;
	}

	public void setServerNum(String serverNum)
	{
		this.serverNum = serverNum;
	}
    
	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}
    
	public Timestamp getSendTime()
	{
		return sendTime;
	}

	public void setSendTime(Timestamp sendTime)
	{
		this.sendTime = sendTime;
	}
    
	public String getMsgType()
	{
		return msgType;
	}

	public void setMsgType(String msgType)
	{
		this.msgType = msgType;
	}
    
	public Integer getPushType()
	{
		return pushType;
	}

	public void setPushType(Integer pushType)
	{
		this.pushType = pushType;
	}

}