package com.montnets.emp.shorturl.report.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class VisitReportVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8182506647682719193L;

	private String phone;
	
	private String taskId;
	
	private Long unicom;
	
	private String errorCode;
	
	private String message;
	
	private String title;
	
	private Timestamp sendTime;

	private Long visitCount;
	
//	private Long visitNum;
	
	/**
	 * 末次访问IP
	 */
	private String lastIP;
	
	/**
	 * 末次访问时间
	 */
	private Timestamp lastVisitTime;
	
	private String areaZone;
	
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

	public Long getVisitCount() {
		return visitCount;
	}

	public void setVisitCount(Long visitCount) {
		this.visitCount = visitCount;
	}

	/*public Long getVisitNum() {
		return visitNum;
	}

	public void setVisitNum(Long visitNum) {
		this.visitNum = visitNum;
	}*/

	public String getLastIP() {
		return lastIP;
	}

	public void setLastIP(String lastIP) {
		this.lastIP = lastIP;
	}

	public Timestamp getLastVisitTime() {
		return lastVisitTime;
	}

	public void setLastVisitTime(Timestamp lastVisitTime) {
		this.lastVisitTime = lastVisitTime;
	}

	public String getAreaZone() {
		return areaZone;
	}

	public void setAreaZone(String areaZone) {
		this.areaZone = areaZone;
	}

}
