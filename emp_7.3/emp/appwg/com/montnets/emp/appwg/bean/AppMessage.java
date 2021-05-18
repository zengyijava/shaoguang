package com.montnets.emp.appwg.bean;

import java.util.LinkedHashSet;
import java.util.List;

import com.montnets.app.style.PMessageStyle;

public class AppMessage
{
	private Long taskId;
	
	//企业编码
	private String	ecode;

	//消息发送者的用户名
	private String	fromUserName;

	//消息接收者的用户名，用;号隔开
	private String	toUserName;
	
	private LinkedHashSet<String> toUserNameSet;

	//消息接收者类型：1、发送所有企业用户，取企业编号；2、发送到某些人，TO为这些人的用户账号，用;号隔开
	private int		toType;
	
	//消息的类型。 	0：文本消息；1：图片消息；2：视频；3：语音；4：事件推送； 
	private int		msgType;
	
	//标题
	private String title;
	
	//消息内容
	private String msgContent;
	
	private String url;

	//有效期限，毫秒数
	private long	validity;
	
	//操作员id
	private Long userId;
	
	private List<PMessageStyle> msgStylesList;
	
	//消息来源，发送个人信息需要设置。 1－企业后台管理； 2－企业EMP； 3－企业客服； 11－安卓客户端； 12－IOS客户端；
	private int msgSrc;

	//设置APP首页用。APP首页轮询大图片，最多支持5张图片
	private List<AppMainPage> mainPageTopList;
	
	//设置APP首页用。图文列表
	private List<AppMainPage> mainPageTitleList;
	
	//公众消息类型。 1：首页推送数据；2：通知、提醒消息
	private int empType;
	
	//发送者姓名
	private String fromName;
	
	//接收者姓名
	private String toName;
	
	private long msgId;
	
	//发送用户数量
	private int sendCount = 0;
	
	//音频或者视频时长，单位为秒
	private long time = 0;
	
	//视频截图url
	private String picUrl;
	
	//app首页id
	private Long sid;
	
	private String localUrl;
	
	private String body;
	
	
	public String getBody()
	{
		return body;
	}

	public void setBody(String body)
	{
		this.body = body;
	}

	public String getLocalUrl()
	{
		return localUrl;
	}

	public void setLocalUrl(String localUrl)
	{
		this.localUrl = localUrl;
	}

	public Long getSid()
	{
		return sid;
	}

	public void setSid(Long sid)
	{
		this.sid = sid;
	}

	public String getPicUrl()
	{
		return picUrl;
	}

	public void setPicUrl(String picUrl)
	{
		this.picUrl = picUrl;
	}

	public long getTime()
	{
		return time;
	}

	public void setTime(long time)
	{
		this.time = time;
	}

	public int getSendCount()
	{
		return sendCount;
	}

	public void setSendCount(int sendCount)
	{
		this.sendCount = sendCount;
	}

	public long getMsgId()
	{
		return msgId;
	}

	public void setMsgId(long msgId)
	{
		this.msgId = msgId;
	}

	public String getFromName()
	{
		return fromName;
	}

	public void setFromName(String fromName)
	{
		this.fromName = fromName;
	}

	public String getToName()
	{
		return toName;
	}

	public void setToName(String toName)
	{
		this.toName = toName;
	}

	public int getEmpType()
	{
		return empType;
	}

	public LinkedHashSet<String> getToUserNameSet()
	{
		return toUserNameSet;
	}

	public void setToUserNameSet(LinkedHashSet<String> toUserNameSet)
	{
		this.toUserNameSet = toUserNameSet;
	}

	public void setEmpType(int empType)
	{
		this.empType = empType;
	}

	public int getMsgSrc()
	{
		return msgSrc;
	}

	public void setMsgSrc(int msgSrc)
	{
		this.msgSrc = msgSrc;
	}

	public List<PMessageStyle> getMsgStylesList()
	{
		return msgStylesList;
	}

	public void setMsgStylesList(List<PMessageStyle> msgStylesList)
	{
		this.msgStylesList = msgStylesList;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public Long getTaskId()
	{
		return taskId;
	}

	public void setTaskId(Long taskId)
	{
		this.taskId = taskId;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getEcode()
	{
		return ecode;
	}

	public void setEcode(String ecode)
	{
		this.ecode = ecode;
	}

	public String getFromUserName()
	{
		return fromUserName;
	}

	public void setFromUserName(String fromUserName)
	{
		this.fromUserName = fromUserName;
	}

	public String getToUserName()
	{
		return toUserName;
	}

	public void setToUserName(String toUserName)
	{
		this.toUserName = toUserName;
	}

	public int getToType()
	{
		return toType;
	}

	public void setToType(int toType)
	{
		this.toType = toType;
	}

	public int getMsgType()
	{
		return msgType;
	}

	public void setMsgType(int msgType)
	{
		this.msgType = msgType;
	}

	public String getMsgContent()
	{
		return msgContent;
	}

	public void setMsgContent(String msgContent)
	{
		this.msgContent = msgContent;
	}

	public long getValidity()
	{
		return validity;
	}

	public void setValidity(long validity)
	{
		this.validity = validity;
	}

	public List<AppMainPage> getMainPageTopList()
	{
		return mainPageTopList;
	}

	public void setMainPageTopList(List<AppMainPage> mainPageTopList)
	{
		this.mainPageTopList = mainPageTopList;
	}

	public List<AppMainPage> getMainPageTitleList()
	{
		return mainPageTitleList;
	}

	public void setMainPageTitleList(List<AppMainPage> mainPageTitleList)
	{
		this.mainPageTitleList = mainPageTitleList;
	}
	
	

}
