package com.montnets.emp.entity.system;

import java.io.Serializable;
import java.sql.Timestamp;

public class MoWaitA implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1614176183434443761L;
	
	private Long id;
	//厂商ID
	private Long ecid;
	//用户UID
	private Long userUid;
	//代理账号UID
	private Long loginUid;
	//用户账号
	private String userId;
	//流水号(平台内短信惟一流水标识)
	private Long ptMsgId;
	//通道号
	private String spNumber;
	//手机号
	private String phone;
	//上行时间
	private Timestamp deliverTime;
	//短信内容
	private String message;
	//运营商:(0: 移动,1: 联通,21: 电信)
	private Integer unicom;
	//协议子段
	private Long tpUdhi;
	//协议子段
	private Long tpPid;
	//长短信分割符标识序号
	private Long longMsgSeq;
	//服务类型
	private String serviceId;
	//主端口
	private String spgate;
	//消息编码格式
	private Long msgFmt;
	private Long pkNumber;
	private Long pkTotal;
	private Long ptNotice;
	//尾号
	private String cpno;
	
	
	public String getCpno() {
		return cpno;
	}
	public void setCpno(String cpno) {
		this.cpno = cpno;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getEcid() {
		return ecid;
	}
	public void setEcid(Long ecid) {
		this.ecid = ecid;
	}
	public Long getUserUid() {
		return userUid;
	}
	public void setUserUid(Long userUid) {
		this.userUid = userUid;
	}
	public Long getLoginUid() {
		return loginUid;
	}
	public void setLoginUid(Long loginUid) {
		this.loginUid = loginUid;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Long getPtMsgId() {
		return ptMsgId;
	}
	public void setPtMsgId(Long ptMsgId) {
		this.ptMsgId = ptMsgId;
	}
	public String getSpNumber() {
		return spNumber;
	}
	public void setSpNumber(String spNumber) {
		this.spNumber = spNumber;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getUnicom() {
		return unicom;
	}
	public void setUnicom(Integer unicom) {
		this.unicom = unicom;
	}
	public Long getTpUdhi() {
		return tpUdhi;
	}
	public void setTpUdhi(Long tpUdhi) {
		this.tpUdhi = tpUdhi;
	}
	public Long getTpPid() {
		return tpPid;
	}
	public void setTpPid(Long tpPid) {
		this.tpPid = tpPid;
	}
	public Long getLongMsgSeq() {
		return longMsgSeq;
	}
	public void setLongMsgSeq(Long longMsgSeq) {
		this.longMsgSeq = longMsgSeq;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getSpgate() {
		return spgate;
	}
	public void setSpgate(String spgate) {
		this.spgate = spgate;
	}
	public Long getMsgFmt() {
		return msgFmt;
	}
	public void setMsgFmt(Long msgFmt) {
		this.msgFmt = msgFmt;
	}
	public Long getPkNumber() {
		return pkNumber;
	}
	public void setPkNumber(Long pkNumber) {
		this.pkNumber = pkNumber;
	}
	public Long getPkTotal() {
		return pkTotal;
	}
	public void setPkTotal(Long pkTotal) {
		this.pkTotal = pkTotal;
	}
	public Long getPtNotice() {
		return ptNotice;
	}
	public void setPtNotice(Long ptNotice) {
		this.ptNotice = ptNotice;
	}
	public void setDeliverTime(Timestamp deliverTime) {
		this.deliverTime = deliverTime;
	}
	public Timestamp getDeliverTime() {
		return deliverTime;
	}
}
