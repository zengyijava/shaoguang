package com.montnets.emp.shorturl.report.vo;

import java.sql.Timestamp;

public class LinkDetailResultVo {

	private String phone;
	
	private String taskId;
	
	private Long unicom;
	
	private String errorCode;
	
	private String message;
	
	private String title;
	
	private Timestamp sendTime;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Long getUnicom() {
		return unicom;
	}

	public void setUnicom(Long unicom) {
		this.unicom = unicom;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Timestamp getSendTime() {
		return sendTime;
	}

	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}

}
