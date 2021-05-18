package com.montnets.emp.perfect.vo;

import java.sql.Timestamp;

public class PerfectNoticeVo implements java.io.Serializable{
	
	private static final long serialVersionUID = -5762906303747559380L;
	private Long noticId;
	private Timestamp submitTime;
	private Long senderGuid;	
	private String senderName;		
	private String content;				
	private Integer recevierType;  	
	//private String  receiverName;
	private Long recevierId ;  			
	private Integer noticCount;	 	
	private Integer replyCount;		
	private Integer receiveCount;	
	private Integer arySendCount;	
	private Long taskId;
	
	//taskid加密字符串
	private String taskIdCipher;
	
	public Long getTaskId()
	{
		return taskId;
	}
	public void setTaskId(Long taskId)
	{
		this.taskId = taskId;
	}
	public Integer getArySendCount()
	{
		return arySendCount;
	}
	public void setArySendCount(Integer arySendCount)
	{
		this.arySendCount = arySendCount;
	}
	public Long getNoticId()
	{
		return noticId;
	}
	public void setNoticId(Long noticId)
	{
		this.noticId = noticId;
	}
	public Timestamp getSubmitTime()
	{
		return submitTime;
	}
	public void setSubmitTime(Timestamp submitTime)
	{
		this.submitTime = submitTime;
	}
	public Long getSenderGuid()
	{
		return senderGuid;
	}
	public void setSenderGuid(Long senderGuid)
	{
		this.senderGuid = senderGuid;
	}
	public String getSenderName()
	{
		return senderName;
	}
	public void setSenderName(String senderName)
	{
		this.senderName = senderName;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public Integer getRecevierType()
	{
		return recevierType;
	}
	public void setRecevierType(Integer recevierType)
	{
		this.recevierType = recevierType;
	}
	public Long getRecevierId()
	{
		return recevierId;
	}
	public void setRecevierId(Long recevierId)
	{
		this.recevierId = recevierId;
	}
	public Integer getNoticCount()
	{
		return noticCount;
	}
	public void setNoticCount(Integer noticCount)
	{
		this.noticCount = noticCount;
	}
	public Integer getReplyCount()
	{
		return replyCount;
	}
	public void setReplyCount(Integer replyCount)
	{
		this.replyCount = replyCount;
	}
	public Integer getReceiveCount()
	{
		return receiveCount;
	}
	public void setReceiveCount(Integer receiveCount)
	{
		this.receiveCount = receiveCount;
	}
	public String getTaskIdCipher() {
		return taskIdCipher;
	}
	public void setTaskIdCipher(String taskIdCipher) {
		this.taskIdCipher = taskIdCipher;
	}
	
}
