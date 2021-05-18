package com.montnets.emp.monitor.constant;

public class MonBusAreaParams
{
	
	//业务编码
	private String busCode;
	//区域码码
	private String areaCode;
	//消息小时
	private int hour;
	//MT已发
	private int mtHaveSnd;
	//消息日期
	private int messageDate;
	
	public MonBusAreaParams()
	{
		
	}

	public String getBusCode()
	{
		return busCode;
	}

	public void setBusCode(String busCode)
	{
		this.busCode = busCode;
	}

	public String getAreaCode()
	{
		return areaCode;
	}

	public void setAreaCode(String areaCode)
	{
		this.areaCode = areaCode;
	}

	public int getHour()
	{
		return hour;
	}

	public void setHour(int hour)
	{
		this.hour = hour;
	}

	public int getMtHaveSnd()
	{
		return mtHaveSnd;
	}

	public void setMtHaveSnd(int mtHaveSnd)
	{
		this.mtHaveSnd = mtHaveSnd;
	}

	public int getMessageDate()
	{
		return messageDate;
	}

	public void setMessageDate(int messageDate)
	{
		this.messageDate = messageDate;
	}
	
}
