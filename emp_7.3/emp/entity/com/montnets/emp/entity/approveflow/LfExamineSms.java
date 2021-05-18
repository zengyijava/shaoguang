package com.montnets.emp.entity.approveflow;

import java.sql.Timestamp;
/**
 * 审批提醒短信表
 * @author Administrator
 *
 */
public class LfExamineSms implements java.io.Serializable{
	public LfExamineSms(){
		
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7183778885409547404L;
	//标识列
	private Long esId;
	//审核流程ID
	private Long frId;
	//手机号码 
	private String phone;
	//短信内容
	private String msgContent;
	//同意批次号
	private String batchNumber;
	//不同意批次号
	private String disagreeNumber;
	//回复内容
	private String reciveContent;
	//回复时间
	private Timestamp reciveTime;
	//通道号
	private String spNumber;
	//发送账号
	private String spUser;
	//1代表短信提醒 2代表是否成功回复
	private Integer esType;
	
	
	public String getSpNumber() {
		return spNumber;
	}
	public void setSpNumber(String spNumber) {
		this.spNumber = spNumber;
	}
	public String getSpUser() {
		return spUser;
	}
	public void setSpUser(String spUser) {
		this.spUser = spUser;
	}
	public Integer getEsType() {
		return esType;
	}
	public void setEsType(Integer esType) {
		this.esType = esType;
	}
	public Long getEsId() {
		return esId;
	}
	public void setEsId(Long esId) {
		this.esId = esId;
	}
	public Long getFrId() {
		return frId;
	}
	public void setFrId(Long frId) {
		this.frId = frId;
	}

	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMsgContent() {
		return msgContent;
	}
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	public String getBatchNumber() {
		return batchNumber;
	}
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	public String getReciveContent() {
		return reciveContent;
	}
	public void setReciveContent(String reciveContent) {
		this.reciveContent = reciveContent;
	}
	public Timestamp getReciveTime() {
		return reciveTime;
	}
	public void setReciveTime(Timestamp reciveTime) {
		this.reciveTime = reciveTime;
	}
	public String getDisagreeNumber() {
		return disagreeNumber;
	}
	public void setDisagreeNumber(String disagreeNumber) {
		this.disagreeNumber = disagreeNumber;
	}

}
