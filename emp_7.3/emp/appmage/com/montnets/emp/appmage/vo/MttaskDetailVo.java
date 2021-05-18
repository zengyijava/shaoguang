package com.montnets.emp.appmage.vo;

import java.sql.Timestamp;

public class MttaskDetailVo implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private String taskid;
	//用户APP账号
	private String appuseraccount;
	//昵称
	private String appusername;
	//发送主题
	private String title;
	//内容
	private String content;
	//发送状态
	private String sendstate;
	//发送时间
	private Timestamp createtime;
	//企业编码
	private String corpcode;
	//回执时间
	private Timestamp recrpttime;
	//回执状态
	private String rptstate;
	

	public String getRptstate()
	{
		return rptstate;
	}
	public void setRptstate(String rptstate)
	{
		this.rptstate = rptstate;
	}
	public Timestamp getRecrpttime()
	{
		return recrpttime;
	}
	public void setRecrpttime(Timestamp recrpttime)
	{
		this.recrpttime = recrpttime;
	}
	public String getCorpcode()
	{
		return corpcode;
	}
	public void setCorpcode(String corpcode)
	{
		this.corpcode = corpcode;
	}
	public String getTaskid()
	{
		return taskid;
	}
	public void setTaskid(String taskid)
	{
		this.taskid = taskid;
	}
	public String getAppuseraccount()
	{
		return appuseraccount;
	}
	public void setAppuseraccount(String appuseraccount)
	{
		this.appuseraccount = appuseraccount;
	}
	public String getAppusername()
	{
		return appusername;
	}
	public void setAppusername(String appusername)
	{
		this.appusername = appusername;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public String getSendstate()
	{
		return sendstate;
	}
	public void setSendstate(String sendstate)
	{
		this.sendstate = sendstate;
	}
	public Timestamp getCreatetime()
	{
		return createtime;
	}
	public void setCreatetime(Timestamp createtime)
	{
		this.createtime = createtime;
	}
	
	
}
