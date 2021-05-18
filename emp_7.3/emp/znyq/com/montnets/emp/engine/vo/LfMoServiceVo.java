package com.montnets.emp.engine.vo;

import java.sql.Timestamp;

/**
 * 上行服务业务vo类
 * @project testofsinolife
 * @author huangzhibin <307260621@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-31 上午10:05:27
 * @description 上行服务业务vo类
 */
public class LfMoServiceVo implements java.io.Serializable
{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	//自增ID
	private Long msId;
	//该上行用户的账号
	private String spUser;
	//上行通道号
	private String spnumber;
	//上行内容
	private String msgContent;
	//接收时间
	private Timestamp deliverTime;
	//上行手机
	private String phone;
	//步骤ID
	private Long prId;
	//业务ID
	private Long serId;
	//回复状态，1-成功，2和空-未回复，3-失败
	private Integer replyState;
	//回复号码内容url
	private String replyUrl;
	//企业编码
	private String corpCode;
	
	//业务名称
	private String serName;
	//指令代码
	private String orderCode;
	
	//客户姓名
	private String clientName;
	//创建者者姓名
	private String createrName;
	
	
	public Long getMsId()
	{
		return msId;
	}
	public void setMsId(Long msId)
	{
		this.msId = msId;
	}
	public String getSpUser()
	{
		return spUser;
	}
	public void setSpUser(String spUser)
	{
		this.spUser = spUser;
	}
	public String getSpnumber()
	{
		return spnumber;
	}
	public void setSpnumber(String spnumber)
	{
		this.spnumber = spnumber;
	}
	public String getMsgContent()
	{
		return msgContent;
	}
	public void setMsgContent(String msgContent)
	{
		this.msgContent = msgContent;
	}
	public Timestamp getDeliverTime()
	{
		return deliverTime;
	}
	public void setDeliverTime(Timestamp deliverTime)
	{
		this.deliverTime = deliverTime;
	}
	public String getPhone()
	{
		return phone;
	}
	public void setPhone(String phone)
	{
		this.phone = phone;
	}
	public Long getPrId()
	{
		return prId;
	}
	public void setPrId(Long prId)
	{
		this.prId = prId;
	}
	public Long getSerId()
	{
		return serId;
	}
	public void setSerId(Long serId)
	{
		this.serId = serId;
	}
	public Integer getReplyState()
	{
		return replyState;
	}
	public void setReplyState(Integer replyState)
	{
		this.replyState = replyState;
	}
	public String getReplyUrl()
	{
		return replyUrl;
	}
	public void setReplyUrl(String replyUrl)
	{
		this.replyUrl = replyUrl;
	}
	public String getCorpCode()
	{
		return corpCode;
	}
	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}
	public String getSerName()
	{
		return serName;
	}
	public void setSerName(String serName)
	{
		this.serName = serName;
	}
	public String getOrderCode()
	{
		return orderCode;
	}
	public void setOrderCode(String orderCode)
	{
		this.orderCode = orderCode;
	}
	public String getClientName()
	{
		return clientName;
	}
	public void setClientName(String clientName)
	{
		this.clientName = clientName;
	}
	public String getCreaterName()
	{
		return createrName;
	}
	public void setCreaterName(String createrName)
	{
		this.createrName = createrName;
	}
	
	
	
}
