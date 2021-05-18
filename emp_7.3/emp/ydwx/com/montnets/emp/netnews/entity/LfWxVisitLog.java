package com.montnets.emp.netnews.entity;

import java.sql.Timestamp;

/**
 * 访问日志对象
 * @project p_ydwx
 * @author huangzhibin <307260621@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-6-8 下午03:02:06
 * @description
 */
public class LfWxVisitLog
{
	private Long id;
	//关联编号
	private Long refid;
	//类型。0：网讯；1：投票
	private Integer type;
	private String phone;
	//访问时间
	private Timestamp visitdate;
	//访问状态。0：允许；1：拒绝
	private Integer visitstatus;
	//拒绝为拒绝的原因
	private String memo;
	private Long uid;
	//访问浏览器
	private String userAgent;
	//访问ip地址
	private String ipAddr;
	//企业编码
	private String corpCode;
	//任务id
	private Long taskid;
	
	
	
	
	public Long getTaskid() {
		return taskid;
	}
	public void setTaskid(Long taskid) {
		this.taskid = taskid;
	}
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public Long getRefid()
	{
		return refid;
	}
	public void setRefid(Long refid)
	{
		this.refid = refid;
	}
	public Integer getType()
	{
		return type;
	}
	public void setType(Integer type)
	{
		this.type = type;
	}
	public String getPhone()
	{
		return phone;
	}
	public void setPhone(String phone)
	{
		this.phone = phone;
	}
	public Timestamp getVisitdate()
	{
		return visitdate;
	}
	public void setVisitdate(Timestamp visitdate)
	{
		this.visitdate = visitdate;
	}
	public Integer getVisitstatus()
	{
		return visitstatus;
	}
	public void setVisitstatus(Integer visitstatus)
	{
		this.visitstatus = visitstatus;
	}
	public String getMemo()
	{
		return memo;
	}
	public void setMemo(String memo)
	{
		this.memo = memo;
	}
	public Long getUid()
	{
		return uid;
	}
	public void setUid(Long uid)
	{
		this.uid = uid;
	}
	public String getUserAgent()
	{
		return userAgent;
	}
	public void setUserAgent(String userAgent)
	{
		this.userAgent = userAgent;
	}
	public String getIpAddr()
	{
		return ipAddr;
	}
	public void setIpAddr(String ipAddr)
	{
		this.ipAddr = ipAddr;
	}
	public String getCorpCode()
	{
		return corpCode;
	}
	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}
	
	
	
	
}
