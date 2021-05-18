package com.montnets.emp.entity.perfect;

import java.sql.Timestamp;

public class LfPerfectNoticUp  implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5950767688759018707L;
	//通知回复标识ID
	private Long pnupId;
	//完美通知ID
	private Long pnotic;
	//发送者GUID	
	private Long senderGuid; 
	//回复内容
	private String content; 
	//发送时间
	private Timestamp sendTime;
	//接收者guid 
	private Long receiverId;
	//接收者类型
	private Integer userType;		
	//名字
	private String name;	
	//是否回复
	private Integer isReply;	
	//是否接收
	private Integer isReceive;	
	//会话ID
	private String dialogId;	
	//任务ID
	private Long taskId;	
	//联系电话
	private String mobile;
	//接收次数
	private Integer receiveCount;
	//发送账号
	private String spUser;
	//全通道号
	private String spNumber;
	//创建时间
	private Timestamp createTime;
	//标志没有接收到状态报告的完美通知 '1':已经接收  ‘2’：接收中   ‘3’：没有状态报告返回
	private String isAtrred;
	
	
	public String getIsAtrred() {
		return isAtrred;
	}

	public void setIsAtrred(String isAtrred) {
		this.isAtrred = isAtrred;
	}

	public String getSpUser() {
		return spUser;
	}

	public void setSpUser(String spUser) {
		this.spUser = spUser;
	}

	public String getSpNumber() {
		return spNumber;
	}

	public void setSpNumber(String spNumber) {
		this.spNumber = spNumber;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	
	
	public LfPerfectNoticUp(){}

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

	public Long getSenderGuid() {
		return senderGuid;
	}

	public void setSenderGuid(Long senderGuid) {
		this.senderGuid = senderGuid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getSendTime() {
		return sendTime;
	}

	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}

	public Long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getIsReply() {
		return isReply;
	}

	public void setIsReply(Integer isReply) {
		this.isReply = isReply;
	}

	public Integer getIsReceive() {
		return isReceive;
	}

	public void setIsReceive(Integer isReceive) {
		this.isReceive = isReceive;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setReceiveCount(Integer receiveCount) {
		this.receiveCount = receiveCount;
	}

	public Integer getReceiveCount() {
		return receiveCount;
	}
	
	
}
