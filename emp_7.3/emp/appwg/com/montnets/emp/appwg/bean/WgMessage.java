package com.montnets.emp.appwg.bean;

import java.sql.Timestamp;

public class WgMessage
{
	private Long id;
	
	private Long msgId;
	
	//读取状态。1：未读；2：已读；
	private Integer readState = 1;
	
	//发送状态。1：未发送；2：发送成功；3：发送失败；4:发送中
	private Integer sendState = 1;
	
	//发送次数
	private Integer sendedCount = 0;
	
	//消息类型。1：公众消息；2：个人消息；
	private Integer msgType;
	
	private String serial;
	private String ecCode;
	private Long appId;
	private String from;
	private String fromName;
	private String to;
	private String toName;
	private Long validity;
	private Integer msgSrc;
	private String body = "";
	
	//公众消息类型。 1：首页推送数据；2：通知、提醒消息
	private Integer emtype;
	
	//消息接收者类型：1:发送所有企业用户，取企业编号；2:发送到某些人，TO为这些人的用户账号，用;号隔开，最多100
	private Integer toType;
	
	private String packetId;
	
	private Timestamp createtime = new Timestamp(System.currentTimeMillis());
	
	//发送结果。状态报告填
	private int sendResult;
	
	private String content1 = "";
	private String content2 = "";
	private String content3 = "";
	private String content4 = "";
	private String content5 = "";
	private String content6 = "";
	private String content7 = "";
	private String content8 = "";
	private String content9 = "";
	private String content10 = "";
	
	private Integer iCode;
	
	//错误码，状态报告填
	private String errCode;
	
	//app账号，状态报告填
	private String userName;
	
	//标记为离线消息。0在线消息；1离线消息
	private Integer outlinemsg;
	
	
	public Integer getOutlinemsg()
	{
		return outlinemsg;
	}
	public void setOutlinemsg(Integer outlinemsg)
	{
		this.outlinemsg = outlinemsg;
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public String getErrCode()
	{
		return errCode;
	}
	public void setErrCode(String errCode)
	{
		this.errCode = errCode;
	}
	public Integer getiCode()
	{
		return iCode;
	}
	public void setiCode(Integer iCode)
	{
		this.iCode = iCode;
	}
	public int getSendResult()
	{
		return sendResult;
	}
	public void setSendResult(int sendResult)
	{
		this.sendResult = sendResult;
	}
	public String getContent1()
	{
		if(content1 == null){
			return "";
		}
		return content1;
	}
	public void setContent1(String content1)
	{
		this.content1 = content1;
	}
	public String getContent2()
	{
		if(content2 == null){
			return "";
		}
		return content2;
	}
	public void setContent2(String content2)
	{
		this.content2 = content2;
	}
	public String getContent3()
	{
		if(content3 == null){
			return "";
		}
		return content3;
	}
	public void setContent3(String content3)
	{
		this.content3 = content3;
	}
	public String getContent4()
	{
		if(content4 == null){
			return "";
		}
		return content4;
	}
	public void setContent4(String content4)
	{
		this.content4 = content4;
	}
	public String getContent5()
	{
		if(content5 == null){
			return "";
		}
		return content5;
	}
	public void setContent5(String content5)
	{
		this.content5 = content5;
	}
	public String getContent6()
	{
		if(content6 == null){
			return "";
		}
		return content6;
	}
	public void setContent6(String content6)
	{
		this.content6 = content6;
	}
	public String getContent7()
	{
		if(content7 == null){
			return "";
		}
		return content7;
	}
	public void setContent7(String content7)
	{
		this.content7 = content7;
	}
	public String getContent8()
	{
		if(content8 == null){
			return "";
		}
		return content8;
	}
	public void setContent8(String content8)
	{
		this.content8 = content8;
	}
	public String getContent9()
	{
		if(content9 == null){
			return "";
		}
		return content9;
	}
	public void setContent9(String content9)
	{
		this.content9 = content9;
	}
	public String getContent10()
	{
		if(content10 == null){
			return "";
		}
		return content10;
	}
	public void setContent10(String content10)
	{
		this.content10 = content10;
	}
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public Timestamp getCreatetime()
	{
		return createtime;
	}
	public void setCreatetime(Timestamp createtime)
	{
		this.createtime = createtime;
	}
	public String getPacketId()
	{
		return packetId;
	}
	public void setPacketId(String packetId)
	{
		this.packetId = packetId;
	}
	public Integer getToType()
	{
		return toType;
	}
	public void setToType(Integer toType)
	{
		this.toType = toType;
	}
	public Integer getEmtype()
	{
		return emtype;
	}
	public void setEmtype(Integer emtype)
	{
		this.emtype = emtype;
	}
	public Long getMsgId()
	{
		return msgId;
	}
	public void setMsgId(Long msgId)
	{
		this.msgId = msgId;
	}
	public Integer getReadState()
	{
		return readState;
	}
	public void setReadState(Integer readState)
	{
		this.readState = readState;
	}
	public Integer getSendState()
	{
		return sendState;
	}
	public void setSendState(Integer sendState)
	{
		this.sendState = sendState;
	}
	public Integer getSendedCount()
	{
		return sendedCount;
	}
	public void setSendedCount(Integer sendedCount)
	{
		this.sendedCount = sendedCount;
	}
	public Integer getMsgType()
	{
		return msgType;
	}
	public void setMsgType(Integer msgType)
	{
		this.msgType = msgType;
	}
	public String getSerial()
	{
		return serial;
	}
	public void setSerial(String serial)
	{
		this.serial = serial;
	}
	public String getEcCode()
	{
		return ecCode;
	}
	public void setEcCode(String ecCode)
	{
		this.ecCode = ecCode;
	}
	public Long getAppId()
	{
		return appId;
	}
	public void setAppId(Long appId)
	{
		this.appId = appId;
	}
	public String getFrom()
	{
		return from;
	}
	public void setFrom(String from)
	{
		this.from = from;
	}
	public String getFromName()
	{
		return fromName;
	}
	public void setFromName(String fromName)
	{
		this.fromName = fromName;
	}
	public String getTo()
	{
		return to;
	}
	public void setTo(String to)
	{
		this.to = to;
	}
	public String getToName()
	{
		return toName;
	}
	public void setToName(String toName)
	{
		this.toName = toName;
	}
	public Long getValidity()
	{
		return validity;
	}
	public void setValidity(Long validity)
	{
		this.validity = validity;
	}
	public Integer getMsgSrc()
	{
		return msgSrc;
	}
	public void setMsgSrc(Integer msgSrc)
	{
		this.msgSrc = msgSrc;
	}
	public String getBody()
	{
		if(body == null){
			return "";
		}
		return body;
	}
	public void setBody(String body)
	{
		this.body = body;
	}
	
	
	
}
