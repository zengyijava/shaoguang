package com.montnets.emp.shorturl.report.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class VisitDetailVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4258330675005782675L;

	/**
	 * 长链接地址
	 */
	private String url;
	
	/**
	 * 号码个数
	 */
	private Long totalNum;
	
	/**
	 * 发送成功数
	 */
	private Long successNum;
	
	/**
	 * 访问人数
	 */
	private Long visivNum;
	
	/**
	 * 访问次数
	 */
	private Long visitCount;
	
	/**
	 * 节点编号
	 */
	private String nodeNo;
	
	/**
	 * 发送主题
	 */
	private String title;
	
	/**
	 * 任务批次
	 */
	private String taskId;

	/**
	 * 发送内容
	 */
	private String msg;
	
	/**
	 * 运营商
	 */
	private Long unicom;
	
	/**
	 * 手机号
	 */
	private String phone;
	
	/**
	 * 区域
	 */
	private String zoneArea;
	
	/**
	 * 接收状态
	 */
	private String errCode; 
	
	/**
	 * 访问状态
	 */
	private String visitStatus;
	
	/**
	 * 末次访问IP
	 */
	private String lastIP;
	
	/**
	 * 末次访问时间
	 */
	private Timestamp lastVisitTime;
	
	/**
	 * 发送时间
	 */
	private Timestamp sendTime;
	
	/**
	 * 开始时间
	 */
	private Timestamp startTime;

	/**
	 * 结束时间
	 */
	private Timestamp endTime;
	
	private Timestamp visitTime;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Long totalNum) {
		this.totalNum = totalNum;
	}

	public Long getSuccessNum() {
		return successNum;
	}

	public void setSuccessNum(Long successNum) {
		this.successNum = successNum;
	}

	public Long getVisivNum() {
		return visivNum;
	}

	public void setVisivNum(Long visivNum) {
		this.visivNum = visivNum;
	}

	public Long getVisitCount() {
		return visitCount;
	}

	public void setVisitCount(Long visitCount) {
		this.visitCount = visitCount;
	}

	public String getNodeNo() {
		return nodeNo;
	}

	public void setNodeNo(String nodeNo) {
		this.nodeNo = nodeNo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Long getUnicom() {
		return unicom;
	}

	public void setUnicom(Long unicom) {
		this.unicom = unicom;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getZoneArea() {
		return zoneArea;
	}

	public void setZoneArea(String zoneArea) {
		this.zoneArea = zoneArea;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getVisitStatus() {
		return visitStatus;
	}

	public void setVisitStatus(String visitStatus) {
		this.visitStatus = visitStatus;
	}

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

	public Timestamp getSendTime() {
		return sendTime;
	}

	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public Timestamp getVisitTime() {
		return visitTime;
	}

	public void setVisitTime(Timestamp visitTime) {
		this.visitTime = visitTime;
	}
	
}
