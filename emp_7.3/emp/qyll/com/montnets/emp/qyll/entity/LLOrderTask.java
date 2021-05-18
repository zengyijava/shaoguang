package com.montnets.emp.qyll.entity;

import java.sql.Timestamp;

public class LLOrderTask {
	private int id; 
	private int taskid; 
	private int ecid; 
	private int user_id; 
	private long org_id; 
	private String topic; 
	private String pro_ids; 
	private String msgtype; 
	private int temp_id; 
	private String sp_user; 
	private String sp_pwd; 
	private int subcount; 
	private int effcount; 
	private int succount; 
	private int faicount; 
	private String timer_status; 
	private Timestamp timer_time; 
	private String re_status; 
	private String orderstatus; 
	private String smsstatus; 
	private String isretry; 
	private Timestamp submittm; 
	private Timestamp ordertm; 
	private Timestamp updatetm; 
	private Timestamp createtm;
	private String orderNo;
	private Double summoney;
	private String msg;
	private String p_ids;
	private String servernum;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTaskid() {
		return taskid;
	}
	public void setTaskid(int taskid) {
		this.taskid = taskid;
	}
	public int getEcid() {
		return ecid;
	}
	public void setEcid(int ecid) {
		this.ecid = ecid;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public long getOrg_id() {
		return org_id;
	}
	public void setOrg_id(long org_id) {
		this.org_id = org_id;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getPro_ids() {
		return pro_ids;
	}
	public void setPro_ids(String pro_ids) {
		this.pro_ids = pro_ids;
	}
	public String getMsgtype() {
		return msgtype;
	}
	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}
	public int getTemp_id() {
		return temp_id;
	}
	public void setTemp_id(int temp_id) {
		this.temp_id = temp_id;
	}
	public String getSp_user() {
		return sp_user;
	}
	public void setSp_user(String sp_user) {
		this.sp_user = sp_user;
	}
	public String getSp_pwd() {
		return sp_pwd;
	}
	public void setSp_pwd(String sp_pwd) {
		this.sp_pwd = sp_pwd;
	}
	public int getSubcount() {
		return subcount;
	}
	public void setSubcount(int subcount) {
		this.subcount = subcount;
	}
	public int getEffcount() {
		return effcount;
	}
	public void setEffcount(int effcount) {
		this.effcount = effcount;
	}
	public int getSuccount() {
		return succount;
	}
	public void setSuccount(int succount) {
		this.succount = succount;
	}
	public int getFaicount() {
		return faicount;
	}
	public void setFaicount(int faicount) {
		this.faicount = faicount;
	}
	public String getTimer_status() {
		return timer_status;
	}
	public void setTimer_status(String timer_status) {
		this.timer_status = timer_status;
	}
	public Timestamp getTimer_time() {
		return timer_time;
	}
	public void setTimer_time(Timestamp timer_time) {
		this.timer_time = timer_time;
	}
	public String getRe_status() {
		return re_status;
	}
	public void setRe_status(String re_status) {
		this.re_status = re_status;
	}
	public String getOrderstatus() {
		return orderstatus;
	}
	public void setOrderstatus(String orderstatus) {
		this.orderstatus = orderstatus;
	}
	public String getSmsstatus() {
		return smsstatus;
	}
	public void setSmsstatus(String smsstatus) {
		this.smsstatus = smsstatus;
	}
	public String getIsretry() {
		return isretry;
	}
	public void setIsretry(String isretry) {
		this.isretry = isretry;
	}
	public Timestamp getSubmittm() {
		return submittm;
	}
	public void setSubmittm(Timestamp submittm) {
		this.submittm = submittm;
	}
	public Timestamp getOrdertm() {
		return ordertm;
	}
	public void setOrdertm(Timestamp ordertm) {
		this.ordertm = ordertm;
	}
	public Timestamp getUpdatetm() {
		return updatetm;
	}
	public void setUpdatetm(Timestamp updatetm) {
		this.updatetm = updatetm;
	}
	public Timestamp getCreatetm() {
		return createtm;
	}
	public void setCreatetm(Timestamp createtm) {
		this.createtm = createtm;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	public Double getSummoney() {
		return summoney;
	}
	public void setSummoney(Double summoney) {
		this.summoney = summoney;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getP_ids() {
		return p_ids;
	}
	public void setP_ids(String p_ids) {
		this.p_ids = p_ids;
	}
	public String getServernum() {
		return servernum;
	}
	public void setServernum(String servernum) {
		this.servernum = servernum;
	}
	@Override
	public String toString() {
		return "LLOrderTask [id=" + id + ", taskid=" + taskid + ", ecid="
				+ ecid + ", user_id=" + user_id + ", org_id=" + org_id
				+ ", topic=" + topic + ", pro_ids=" + pro_ids + ", msgtype="
				+ msgtype + ", temp_id=" + temp_id + ", sp_user=" + sp_user
				+ ", sp_pwd=" + sp_pwd + ", subcount=" + subcount
				+ ", effcount=" + effcount + ", succount=" + succount
				+ ", faicount=" + faicount + ", timer_status=" + timer_status
				+ ", timer_time=" + timer_time + ", re_status=" + re_status
				+ ", orderstatus=" + orderstatus + ", smsstatus=" + smsstatus
				+ ", isretry=" + isretry + ", submittm=" + submittm
				+ ", ordertm=" + ordertm + ", updatetm=" + updatetm
				+ ", createtm=" + createtm + ", orderNo=" + orderNo
				+ ", summoney=" + summoney + ", msg=" + msg + ", p_ids="
				+ p_ids + ", servernum=" + servernum + "]";
	}
	 
	

	
}
