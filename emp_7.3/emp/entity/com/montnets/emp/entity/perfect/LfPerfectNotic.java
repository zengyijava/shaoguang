package com.montnets.emp.entity.perfect;

import java.sql.Timestamp;

public class LfPerfectNotic  implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3120810186456004723L;
	private Long noticId;
	private Timestamp submitTime;
	private Integer sendInterval;
	private Integer maxSendCount;
	private Integer arySendCount;
	private Long senderGuid;		
	
	private String content;				
	private Integer recevierType;  		
	private Long recevierId ;  			
	private String dialogId ;			
	private Long taskId;      			
	private Integer noticCount;	

	private String corpCode;
	//private String receiverGuidList;
	//private String aryReceiverGuidList;


	public Long getNoticId() {
		return noticId;
	}

	public void setNoticId(Long noticId) {
		this.noticId = noticId;
	}

	public Integer getSendInterval() {
		return sendInterval;
	}

	public void setSendInterval(Integer sendInterval) {
		this.sendInterval = sendInterval;
	}

	public Integer getMaxSendCount() {
		return maxSendCount;
	}

	public void setMaxSendCount(Integer maxSendCount) {
		this.maxSendCount = maxSendCount;
	}

	public Integer getArySendCount() {
		return arySendCount;
	}

	public void setArySendCount(Integer arySendCount) {
		this.arySendCount = arySendCount;
	}

	public Long getSenderGuid() {
		return senderGuid;
	}

	public void setSenderGuid(Long senderGuid) {
		this.senderGuid = senderGuid;
	}

	public Timestamp getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Timestamp submitTime) {
		this.submitTime = submitTime;
	}
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getRecevierType() {
		return recevierType;
	}

	public void setRecevierType(Integer recevierType) {
		this.recevierType = recevierType;
	}

	public Long getRecevierId() {
		return recevierId;
	}

	public void setRecevierId(Long recevierId) {
		this.recevierId = recevierId;
	}

	public String getDialogId() {
		return dialogId;
	}

	public void setDialogId(String dialogId) {
		this.dialogId = dialogId;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public void setNoticCount(Integer noticCount) {
		this.noticCount = noticCount;
	}

	public Integer getNoticCount() {
		return noticCount;
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
