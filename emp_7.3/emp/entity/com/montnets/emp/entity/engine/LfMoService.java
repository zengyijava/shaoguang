/**
 * 
 */
package com.montnets.emp.entity.engine;

import java.sql.Timestamp;

/**
 * 上行服务业务
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-4-20 下午02:18:36
 * @description TableLfMoService对应的实体类
 */

public class LfMoService implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3358422698324365274L;
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

	public LfMoService()
	{
		this.deliverTime = new Timestamp(System.currentTimeMillis());
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
	
}
