package com.montnets.emp.common.bean;

public class BalanceAlarmBean
{
	//账号
	private String spUser;
	//账号类型
	private Integer accountType;
	//状态码
	private String stateCode;
	//错误信息
	private String errInfo;
	public String getSpUser()
	{
		return spUser;
	}
	public void setSpUser(String spUser)
	{
		this.spUser = spUser;
	}
	

	public Integer getAccountType()
	{
		return accountType;
	}
	public void setAccountType(Integer accountType)
	{
		this.accountType = accountType;
	}
	public String getStateCode()
	{
		return stateCode;
	}
	public void setStateCode(String stateCode)
	{
		this.stateCode = stateCode;
	}
	public String getErrInfo()
	{
		return errInfo;
	}
	public void setErrInfo(String errInfo)
	{
		this.errInfo = errInfo;
	}
	
	
}
