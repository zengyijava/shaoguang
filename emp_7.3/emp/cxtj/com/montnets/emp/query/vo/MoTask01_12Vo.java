package com.montnets.emp.query.vo;

import java.sql.Timestamp;

/**
 * 
 * @project emp
 * @author 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-4-1 上午09:27:34
 * @description
 */
public class MoTask01_12Vo implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2290315435947656673L;

	private String spnumber;

	private Timestamp deliverTime;

	private String phone;
	
	private String name;

	private String msgContent;

	private String startSubmitTime;

	private String endSubmitTime;
	
    private String spUser;//历史记录用发送账号

	private Long msgFmt;//信息编码格式编码
	
	private String userId;//发送账号
	
	private Integer unicom;//运行商（0移动，1联通，21电信，5国外）
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpUser() {
		return spUser;
	}

	public void setSpUser(String spUser) {
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

	public String getMsgContent()
	{
		return msgContent;
	}

	public void setMsgContent(String msgContent)
	{
		this.msgContent = msgContent;
	}

	public String getStartSubmitTime()
	{
		return startSubmitTime;
	}

	public void setStartSubmitTime(String startSubmitTime)
	{
		this.startSubmitTime = startSubmitTime;
	}

	public String getEndSubmitTime()
	{
		return endSubmitTime;
	}

	public void setEndSubmitTime(String endSubmitTime)
	{
		this.endSubmitTime = endSubmitTime;
	}

	public Long getMsgFmt()
	{
		return msgFmt;
	}

	public void setMsgFmt(Long msgFmt)
	{
		this.msgFmt = msgFmt;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public Integer getUnicom()
	{
		return unicom;
	}

	public void setUnicom(Integer unicom)
	{
		this.unicom = unicom;
	}
	
}
