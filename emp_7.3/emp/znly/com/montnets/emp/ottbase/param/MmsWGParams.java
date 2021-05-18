package com.montnets.emp.ottbase.param;

public class MmsWGParams
{
	//彩信发送账号(必填)
	private String userID;
	
	//信息类型(必填)
	//0短信,10普通彩信,11静态模板彩信,12动态模板彩信
	private  Integer msgType;
	
	//任务ID(必填)
	private Integer taskid;
	
	//彩信标题(必填)
	private String title;
	
	//模板ID(模板彩信时填写，普通彩信不填)
	//模板彩信时填模板ID，普通彩信时不填
	private Long tmpID;
	
	//信息内容(普通彩信填写，模板彩信不填)
	//彩信内容,普通彩信时这里填彩信文件路径,模板彩信时不填
	private String msg;
	
	//文件远程地址(必填)
	private String remoteUrl;
	
	//发送状态 (必填)
	//1收到请求(初始状态)
	private Integer sendStatus;
	
	//业务类型(选填)
	private String svrType;
	
	//参数P1(选填)
	private String p1;
	
	//参数P2(选填)
	private String p2;
	
	//参数P3(选填)
	private String p3;
	
	//参数P4(选填)
	private String p4;
	
	//模块ID(选填)
	private Integer moduleID;

	public String getUserID()
	{
		return userID;
	}

	public void setUserID(String userID)
	{
		this.userID = userID;
	}

	public Integer getMsgType()
	{
		return msgType;
	}

	public void setMsgType(Integer msgType)
	{
		this.msgType = msgType;
	}

	public Integer getTaskid()
	{
		return taskid;
	}

	public void setTaskid(Integer taskid)
	{
		this.taskid = taskid;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public Long getTmpID()
	{
		return tmpID;
	}

	public void setTmpID(Long tmpID)
	{
		this.tmpID = tmpID;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public String getRemoteUrl()
	{
		return remoteUrl;
	}

	public void setRemoteUrl(String remoteUrl)
	{
		this.remoteUrl = remoteUrl;
	}

	public Integer getSendStatus()
	{
		return sendStatus;
	}

	public void setSendStatus(Integer sendStatus)
	{
		this.sendStatus = sendStatus;
	}

	public String getSvrType()
	{
		return svrType;
	}

	public void setSvrType(String svrType)
	{
		this.svrType = svrType;
	}

	public String getP1()
	{
		return p1;
	}

	public void setP1(String p1)
	{
		this.p1 = p1;
	}

	public String getP2()
	{
		return p2;
	}

	public void setP2(String p2)
	{
		this.p2 = p2;
	}

	public String getP3()
	{
		return p3;
	}

	public void setP3(String p3)
	{
		this.p3 = p3;
	}

	public String getP4()
	{
		return p4;
	}

	public void setP4(String p4)
	{
		this.p4 = p4;
	}

	public Integer getModuleID()
	{
		return moduleID;
	}

	public void setModuleID(Integer moduleID)
	{
		this.moduleID = moduleID;
	}
	
}
