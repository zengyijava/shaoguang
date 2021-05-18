package com.montnets.emp.entity.sms;

import java.sql.Timestamp;

import sun.security.util.BigInt;

/**
 * TableMtTaskC对应的实体类 网关短信滞留表
 * 
 * @project emp
 * @author linzhihan <zhihanking@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2012-5-16 下午14:18:50
 * @description
 */
public class MtTaskC implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6694960785371073680L;

	private Long id;
	private String ptMsgId;
	private Long uid;
	// private Long ecid;
	private String userId;
	private String spgate;
	private String cpno;
	private String shouji;
	// private String phone;
	private Long spMsgId;
	private Long retFlag;
	private Long feeFlag;
	private Long tpudhi;
	private Long pknumber;
	private Long pktotal;
	private Long sendStatus;
	private Long sendFlag;
	private Long recvFlag;
	private String doneDate;
	private String errorCode;
	private Long sendLevel;
	private Long sendType;
	private Long unicom;
	private Timestamp sendTime;
	private Long validTime;
	private String message;
	private String loginid;
	private Integer msgfmt;
	// private Long resendcnt;
	private Long taskId;

	public MtTaskC() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPtMsgId() {
		return ptMsgId;
	}

	public void setPtMsgId(String ptMsgId) {
		this.ptMsgId = ptMsgId;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSpgate() {
		return spgate;
	}

	public void setSpgate(String spgate) {
		this.spgate = spgate;
	}

	public String getCpno() {
		return cpno;
	}

	public void setCpno(String cpno) {
		this.cpno = cpno;
	}

	public String getShouji() {
		return shouji;
	}

	public void setShouji(String shouji) {
		this.shouji = shouji;
	}

	public Long getSpMsgId() {
		return spMsgId;
	}

	public void setSpMsgId(Long spMsgId) {
		this.spMsgId = spMsgId;
	}

	public Long getRetFlag() {
		return retFlag;
	}

	public void setRetFlag(Long retFlag) {
		this.retFlag = retFlag;
	}

	public Long getFeeFlag() {
		return feeFlag;
	}

	public void setFeeFlag(Long feeFlag) {
		this.feeFlag = feeFlag;
	}

	public Long getTpudhi() {
		return tpudhi;
	}

	public void setTpudhi(Long tpudhi) {
		this.tpudhi = tpudhi;
	}

	public Long getPknumber() {
		return pknumber;
	}

	public void setPknumber(Long pknumber) {
		this.pknumber = pknumber;
	}

	public Long getPktotal() {
		return pktotal;
	}

	public void setPktotal(Long pktotal) {
		this.pktotal = pktotal;
	}

	public Long getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(Long sendStatus) {
		this.sendStatus = sendStatus;
	}

	public Long getSendFlag() {
		return sendFlag;
	}

	public void setSendFlag(Long sendFlag) {
		this.sendFlag = sendFlag;
	}

	public Long getRecvFlag() {
		return recvFlag;
	}

	public void setRecvFlag(Long recvFlag) {
		this.recvFlag = recvFlag;
	}

	public String getDoneDate() {
		return doneDate;
	}

	public void setDoneDate(String doneDate) {
		this.doneDate = doneDate;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public Long getSendLevel() {
		return sendLevel;
	}

	public void setSendLevel(Long sendLevel) {
		this.sendLevel = sendLevel;
	}

	public Long getSendType() {
		return sendType;
	}

	public void setSendType(Long sendType) {
		this.sendType = sendType;
	}

	public Long getUnicom() {
		return unicom;
	}

	public void setUnicom(Long unicom) {
		this.unicom = unicom;
	}

	public Timestamp getSendTime() {
		return sendTime;
	}

	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}


	public Long getValidTime()
	{
		return validTime;
	}

	public void setValidTime(Long validTime)
	{
		this.validTime = validTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLoginid() {
		return loginid;
	}

	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}

	public Integer getMsgfmt() {
		return msgfmt;
	}

	public void setMsgfmt(Integer msgfmt) {
		this.msgfmt = msgfmt;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

}