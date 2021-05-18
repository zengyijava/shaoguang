package com.montnets.emp.entity.monitor;

import java.sql.Timestamp;

public class LfBusareasend
{
	// 业务编码
	private String		buscode;

	// 发送总数
	private String		mtsendcount;

	// 业务名称
	private String		busname;

	// 数据时间
	private Integer datadate;

	// 自增ID
	private Long		id;

	// 区域
	private String		areacode;

	// 最后一次更新时间
	private Timestamp	updatetime;

	// 企业编码
	private String		corpcode;
	
	//网关编号
	private Long gatewayid;

	public LfBusareasend()
	{
	}

	public String getBuscode()
	{

		return buscode;
	}

	public void setBuscode(String buscode)
	{

		this.buscode = buscode;

	}

	public String getMtsendcount()
	{

		return mtsendcount;
	}

	public void setMtsendcount(String mtsendcount)
	{

		this.mtsendcount = mtsendcount;

	}

	public String getBusname()
	{

		return busname;
	}

	public void setBusname(String busname)
	{

		this.busname = busname;

	}



	public Integer getDatadate()
	{
		return datadate;
	}

	public void setDatadate(Integer datadate)
	{
		this.datadate = datadate;
	}

	public Long getId()
	{

		return id;
	}

	public void setId(Long id)
	{

		this.id = id;

	}

	public String getAreacode()
	{

		return areacode;
	}

	public void setAreacode(String areacode)
	{

		this.areacode = areacode;

	}

	public Timestamp getUpdatetime()
	{

		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime)
	{

		this.updatetime = updatetime;

	}

	public String getCorpcode()
	{

		return corpcode;
	}

	public void setCorpcode(String corpcode)
	{

		this.corpcode = corpcode;

	}

	public Long getGatewayid()
	{
		return gatewayid;
	}

	public void setGatewayid(Long gatewayid)
	{
		this.gatewayid = gatewayid;
	}

}
