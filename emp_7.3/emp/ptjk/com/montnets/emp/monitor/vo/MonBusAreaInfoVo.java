package com.montnets.emp.monitor.vo;

import java.sql.Timestamp;

public class MonBusAreaInfoVo implements java.io.Serializable
{

	/**
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-11-26 下午03:30:44
	 */
	private static final long	serialVersionUID	= 3805530709913483971L;

	// 业务名称
	private String				busName;

	// 业务编码
	private String				busCode;

	// 区域
	private String				areaCode;

	// 告警通知手机号
	private String				monPhone;

	// 告警通知邮件
	private String				monemail;
	
	// 监控开始时间
	private Timestamp			beginTime;

	// 监控结束时间
	private Timestamp			endTime;

	// LF_MON_BUSDATA 自增ID
	private Long				id;

	// 业务ID
	private Long				busBaseId;

	// 开始时间段(小时)
	private Integer				beginHour;

	// 结束时间段(小时)
	private Integer				endHour;

	// MT已发告警值
	private Integer				mtHaveSnd;

	// 偏离率（高）
	private Integer				deviatHigh;

	// 偏离率（低）
	private Integer				deviatLow;

	// 最后一次告警时间
	private Integer				monLastTime;

	// 企业编码
	private String				corpCode;

	// 创建时间
	private Timestamp			createTime;

	public MonBusAreaInfoVo()
	{

	}

	public String getBusName()
	{
		return busName;
	}

	public void setBusName(String busName)
	{
		this.busName = busName;
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

	public String getMonPhone()
	{
		return monPhone;
	}

	public void setMonPhone(String monPhone)
	{
		this.monPhone = monPhone;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getBusBaseId()
	{
		return busBaseId;
	}

	public void setBusBaseId(Long busBaseId)
	{
		this.busBaseId = busBaseId;
	}

	public Integer getBeginHour()
	{
		return beginHour;
	}

	public void setBeginHour(Integer beginHour)
	{
		this.beginHour = beginHour;
	}

	public Integer getEndHour()
	{
		return endHour;
	}

	public void setEndHour(Integer endHour)
	{
		this.endHour = endHour;
	}

	public Integer getMtHaveSnd()
	{
		return mtHaveSnd;
	}

	public void setMtHaveSnd(Integer mtHaveSnd)
	{
		this.mtHaveSnd = mtHaveSnd;
	}

	public Integer getDeviatHigh()
	{
		return deviatHigh;
	}

	public void setDeviatHigh(Integer deviatHigh)
	{
		this.deviatHigh = deviatHigh;
	}

	public Integer getDeviatLow()
	{
		return deviatLow;
	}

	public void setDeviatLow(Integer deviatLow)
	{
		this.deviatLow = deviatLow;
	}

	public Integer getMonLastTime()
	{
		return monLastTime;
	}

	public void setMonLastTime(Integer monLastTime)
	{
		this.monLastTime = monLastTime;
	}

	public Timestamp getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Timestamp createTime)
	{
		this.createTime = createTime;
	}

	public Timestamp getBeginTime()
	{
		return beginTime;
	}

	public void setBeginTime(Timestamp beginTime)
	{
		this.beginTime = beginTime;
	}

	public Timestamp getEndTime()
	{
		return endTime;
	}

	public void setEndTime(Timestamp endTime)
	{
		this.endTime = endTime;
	}

	public String getCorpCode()
	{
		return corpCode;
	}

	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}

	public String getMonemail() {
		return monemail;
	}

	public void setMonemail(String monemail) {
		this.monemail = monemail;
	}
}
