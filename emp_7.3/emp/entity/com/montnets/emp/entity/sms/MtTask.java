package com.montnets.emp.entity.sms;

import java.sql.Timestamp;


/**
 * TableMtTask对应的实体类
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:28:50
 * @description 
 */
public class MtTask implements java.io.Serializable {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = -2418205366822958948L;
	private Long id;
	private Long ptMsgId;
	private Long uid;
	private Long ecid;
	private String userId;
	private String spgate;
	private String cpno;
	private String phone;
	private Long spMsgId;
	private Long retFlag;
	private Long feeFlag;
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
	private Timestamp recvTime;
	private String message;
	private Long resendcnt;
	private Long taskId;
  
	private Long rsucc;
	private double fee;
	private int spisuncm;
    private String time;
    
    private Integer msgfmt;
    
    private String svrtype;
    
    //任务批次号
    private Long batchID;
    
	public Long getBatchID()
	{
		return batchID;
	}


	public void setBatchID(Long batchID)
	{
		this.batchID = batchID;
	}


	public MtTask() {}

    
	public String getSvrtype() {
		return svrtype;
	}
	public void setSvrtype(String svrtype) {
		this.svrtype = svrtype;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getPtMsgId()
	{
		return ptMsgId;
	}


	public void setPtMsgId(Long ptMsgId)
	{
		this.ptMsgId = ptMsgId;
	}


	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	public Long getEcid() {
		return ecid;
	}
	public void setEcid(Long ecid) {
		this.ecid = ecid;
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
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
	public Timestamp getRecvTime() {
		return recvTime;
	}
	public void setRecvTime(Timestamp recvTime) {
		this.recvTime = recvTime;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Long getResendcnt() {
		return resendcnt;
	}
	public void setResendcnt(Long resendcnt) {
		this.resendcnt = resendcnt;
	}
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}


	public Long getRsucc() {
		return rsucc;
	}


	public void setRsucc(Long rsucc) {
		this.rsucc = rsucc;
	}


	public double getFee() {
		return fee;
	}


	public void setFee(double fee) {
		this.fee = fee;
	}


	public int getSpisuncm() {
		return spisuncm;
	}


	public void setSpisuncm(int spisuncm) {
		this.spisuncm = spisuncm;
	}


	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public Integer getMsgfmt() {
		return msgfmt;
	}


	public void setMsgfmt(Integer msgfmt) {
		this.msgfmt = msgfmt;
	}

 
	


	
   
}