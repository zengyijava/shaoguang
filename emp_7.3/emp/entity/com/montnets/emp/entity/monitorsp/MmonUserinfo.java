package com.montnets.emp.entity.monitorsp;

import java.sql.Timestamp;

public class MmonUserinfo implements java.io.Serializable{

	private static final long serialVersionUID = -8346356944809528395L;

	private String ptcode;
	//用户id
	private String userId;
	
	private Integer userUid;
	//用户名称
	private String userName;
	
	private Integer userPrivilege;
	
	private String jtype;
	
	private Integer linkNum;
	//登录ip
	private String loginIp;
	//上线状态
	private Integer onLineStatus;
	
	private Integer userFee;
	
	private Integer fixFailureRate;	
	
	private Integer failureNum;
	
	private Integer failureRate;
	
	private String bindInfo;
	
	private Integer mtTotalSnd;
	
	private Integer mtHaveSnd;
	
	private Integer mtRemained;
	
	private String mtSndInfo;
	
	private Integer mtSndSpd;
	
	private Integer moTotalRec;
	
	private Integer moRemained;
	
	private Integer moRptRecvSpd;
	
	private Integer moTmoutCnt;
	
	private Integer rptTotalRecv;
	
	private Integer rptRemained;
	
	private Integer rptTmoutCnt;
	
	private String loginInTm;
	
	private String loginOutTm;
	//修改时间
	private Timestamp updateTime;	
	
	public MmonUserinfo(){}

	public String getPtcode() {
		return ptcode;
	}

	public void setPtcode(String ptcode) {
		this.ptcode = ptcode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getUserUid() {
		return userUid;
	}

	public void setUserUid(Integer userUid) {
		this.userUid = userUid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getUserPrivilege() {
		return userPrivilege;
	}

	public void setUserPrivilege(Integer userPrivilege) {
		this.userPrivilege = userPrivilege;
	}

	public String getJtype() {
		return jtype;
	}

	public void setJtype(String jtype) {
		this.jtype = jtype;
	}

	public Integer getLinkNum() {
		return linkNum;
	}

	public void setLinkNum(Integer linkNum) {
		this.linkNum = linkNum;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Integer getOnLineStatus() {
		return onLineStatus;
	}

	public void setOnLineStatus(Integer onLineStatus) {
		this.onLineStatus = onLineStatus;
	}

	public Integer getUserFee() {
		return userFee;
	}

	public void setUserFee(Integer userFee) {
		this.userFee = userFee;
	}

	public Integer getFixFailureRate() {
		return fixFailureRate;
	}

	public void setFixFailureRate(Integer fixFailureRate) {
		this.fixFailureRate = fixFailureRate;
	}

	public Integer getFailureNum() {
		return failureNum;
	}

	public void setFailureNum(Integer failureNum) {
		this.failureNum = failureNum;
	}

	public Integer getFailureRate() {
		return failureRate;
	}

	public void setFailureRate(Integer failureRate) {
		this.failureRate = failureRate;
	}

	public String getBindInfo() {
		return bindInfo;
	}

	public void setBindInfo(String bindInfo) {
		this.bindInfo = bindInfo;
	}

	public Integer getMtTotalSnd() {
		return mtTotalSnd;
	}

	public void setMtTotalSnd(Integer mtTotalSnd) {
		this.mtTotalSnd = mtTotalSnd;
	}

	public Integer getMtHaveSnd() {
		return mtHaveSnd;
	}

	public void setMtHaveSnd(Integer mtHaveSnd) {
		this.mtHaveSnd = mtHaveSnd;
	}

	public Integer getMtRemained() {
		return mtRemained;
	}

	public void setMtRemained(Integer mtRemained) {
		this.mtRemained = mtRemained;
	}

	public String getMtSndInfo() {
		return mtSndInfo;
	}

	public void setMtSndInfo(String mtSndInfo) {
		this.mtSndInfo = mtSndInfo;
	}

	public Integer getMtSndSpd() {
		return mtSndSpd;
	}

	public void setMtSndSpd(Integer mtSndSpd) {
		this.mtSndSpd = mtSndSpd;
	}

	public Integer getMoTotalRec() {
		return moTotalRec;
	}

	public void setMoTotalRec(Integer moTotalRec) {
		this.moTotalRec = moTotalRec;
	}

	public Integer getMoRemained() {
		return moRemained;
	}

	public void setMoRemained(Integer moRemained) {
		this.moRemained = moRemained;
	}

	public Integer getMoRptRecvSpd() {
		return moRptRecvSpd;
	}

	public void setMoRptRecvSpd(Integer moRptRecvSpd) {
		this.moRptRecvSpd = moRptRecvSpd;
	}

	public Integer getMoTmoutCnt() {
		return moTmoutCnt;
	}

	public void setMoTmoutCnt(Integer moTmoutCnt) {
		this.moTmoutCnt = moTmoutCnt;
	}

	public Integer getRptTotalRecv() {
		return rptTotalRecv;
	}

	public void setRptTotalRecv(Integer rptTotalRecv) {
		this.rptTotalRecv = rptTotalRecv;
	}

	public Integer getRptRemained() {
		return rptRemained;
	}

	public void setRptRemained(Integer rptRemained) {
		this.rptRemained = rptRemained;
	}

	public Integer getRptTmoutCnt() {
		return rptTmoutCnt;
	}

	public void setRptTmoutCnt(Integer rptTmoutCnt) {
		this.rptTmoutCnt = rptTmoutCnt;
	}

	public String getLoginInTm() {
		return loginInTm;
	}

	public void setLoginInTm(String loginInTm) {
		this.loginInTm = loginInTm;
	}

	public String getLoginOutTm() {
		return loginOutTm;
	}

	public void setLoginOutTm(String loginOutTm) {
		this.loginOutTm = loginOutTm;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}	
}
