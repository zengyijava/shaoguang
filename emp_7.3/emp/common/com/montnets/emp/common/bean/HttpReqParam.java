package com.montnets.emp.common.bean;

public class HttpReqParam
{
	//请求命令
	private String command;
	
	//发送者timeServerID
	private String senderSerID;
	
	//接收者timeServerID
	private String recerSerID;
	
	//处理状态。true为接收管理权
	private String dealState;
	
	

	public String getCommand()
	{
		return command;
	}

	public void setCommand(String command)
	{
		this.command = command;
	}

	public String getSenderSerID()
	{
		return senderSerID;
	}

	public void setSenderSerID(String senderSerID)
	{
		this.senderSerID = senderSerID;
	}

	public String getRecerSerID()
	{
		return recerSerID;
	}

	public void setRecerSerID(String recerSerID)
	{
		this.recerSerID = recerSerID;
	}

	public String getDealState()
	{
		return dealState;
	}

	public void setDealState(String dealState)
	{
		this.dealState = dealState;
	}
	
	
}
