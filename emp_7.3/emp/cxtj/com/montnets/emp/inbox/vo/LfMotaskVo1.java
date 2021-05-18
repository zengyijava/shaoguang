package com.montnets.emp.inbox.vo;

import java.sql.Timestamp;

public class LfMotaskVo1 {
	//标识ID
	private Long moId;
	// 该上行所属用户的账号
	private String spUser;
	// 上行通道
	private String spnumber;
	// 消息编码格式
	private Long msgFmt;
	// 接收该上行的时间
	private Timestamp deliverTime;
	// 上行手机号
	private String phone;
	// 上行内容
	private String msgContent;
	//所属企业编码
	private String corpCode;
	//0移动，1联通，21电信
	private Integer spisuncm;
	//用户userid
	private Long userId;
	
	//用户guid
	private Long userguid;
    //用户名称
	private String name;
	//机构id
	private Long sysdepId;
	
	private String employeeName;
	
	public Long getUserguid()
	{
		return userguid;
	}
	public void setUserguid(Long userguid)
	{
		this.userguid = userguid;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public Long getSysdepId()
	{
		return sysdepId;
	}
	public void setSysdepId(Long sysdepId)
	{
		this.sysdepId = sysdepId;
	}
	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}
	
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
	

	public Integer getSpisuncm() {
		return spisuncm;
	}

	public void setSpisuncm(Integer spisuncm) {
		this.spisuncm = spisuncm;
	}




	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public Long getMoId()
	{
		return moId;
	}

	public void setMoId(Long moId)
	{
		this.moId = moId;
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

	public Long getMsgFmt()
	{
		return msgFmt;
	}

	public void setMsgFmt(Long msgFmt)
	{
		this.msgFmt = msgFmt;
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


}
