package com.montnets.emp.perfect.vo;


public class PerfectNoticUpVo {
	//通知回复标识ID
	private Long pnupId;
	//完美通知ID
	private Long pnotic;
	//回复内容
	private String content; 
	//发送时间
	private String sendTime;
	//接收者guid 
	private Long receiverId;	
	//名字
	private String name;	
	//是否回复,所对应的提示信息
	private String isReplyMsg;	
	//是否接收,所对应的提示信息
	private String isReceiveMsg;	
	//联系电话
	private String mobile;
	//接收次数,所对应的标志    次数或者 /”-“
	private String receiveCountMsg;
	
	//标识该条完美通知是否可以再发送   1是可以  2是不可以
	private String isValid;
	public Long getPnupId() {
		return pnupId;
	}

	public void setPnupId(Long pnupId) {
		this.pnupId = pnupId;
	}

	public Long getPnotic() {
		return pnotic;
	}

	public void setPnotic(Long pnotic) {
		this.pnotic = pnotic;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public Long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIsReplyMsg() {
		return isReplyMsg;
	}

	public void setIsReplyMsg(String isReplyMsg) {
		this.isReplyMsg = isReplyMsg;
	}

	public String getIsReceiveMsg() {
		return isReceiveMsg;
	}

	public void setIsReceiveMsg(String isReceiveMsg) {
		this.isReceiveMsg = isReceiveMsg;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getReceiveCountMsg() {
		return receiveCountMsg;
	}

	public void setReceiveCountMsg(String receiveCountMsg) {
		this.receiveCountMsg = receiveCountMsg;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}


	
	
	
	
	
	
	
	
	
	
	
	
}
