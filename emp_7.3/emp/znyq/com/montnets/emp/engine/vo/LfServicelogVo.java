package com.montnets.emp.engine.vo;

import java.sql.Timestamp;

public class LfServicelogVo implements java.io.Serializable
{
	/**
	 * 日志vo
	 */
	private static final long serialVersionUID = 563183730703839765L;
   //主键 
	private Long slId;
	//运行时间
	private Timestamp runTime;
	//状态
	private Integer slState;
	//地址
	private String url;
    //发送账号
	private String spUser;
	//服务名称
	private String serName;
    //名称
	private String name;
	//业务id
	private Long serId;
	//姓名
	private String staffName;
	//开始提交时间
	private String startSubmitTime;
    //结束提交时间
	private String endSubmitTime;	
	//1表示上行业务，2表示下行业务
	private Integer serType;
	//获取业务类型
	public Integer getSerType() {
		return serType;
	}
	//设置业务类型
	public void setSerType(Integer serType) {
		this.serType = serType;
	}
	
	public Long getSerId()
	{
		return serId;
	}
	public void setSerId(Long serId)
	{
		this.serId = serId;
	}
	public Long getSlId()
	{
		return slId;
	}

	public void setSlId(Long slId)
	{
		this.slId = slId;
	}

	public Timestamp getRunTime()
	{
		return runTime;
	}

	public void setRunTime(Timestamp runTime)
	{
		this.runTime = runTime;
	}

	public Integer getSlState()
	{
		return slState;
	}

	public void setSlState(Integer slState)
	{
		this.slState = slState;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getSpUser()
	{
		return spUser;
	}

	public void setSpUser(String spUser)
	{
		this.spUser = spUser;
	}

	public String getStaffName()
	{
		return staffName;
	}

	public void setStaffName(String staffName)
	{
		this.staffName = staffName;
	}

	public String getSerName()
	{
		return serName;
	}

	public void setSerName(String serName)
	{
		this.serName = serName;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
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
}
