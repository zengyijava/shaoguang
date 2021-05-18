package com.montnets.emp.entity.monitorpas;

import java.sql.Timestamp;

public class MmonSpateinfo implements java.io.Serializable{

	private static final long serialVersionUID = 5114646993048311424L;

	private String ptcode;
	//用户id
	private String userId;
	
	private Integer userUid;
	//用户名称
	private String userName;
	
	private String jtype;
	
	private Integer linkNum;
	//登录ip
	private String loginIp;
	//上线状态
	private Integer onLineStatus;
	
	private Integer mtHaveSnd;	
	
	private Integer mtRemained;
	
	private Integer mtRecvSpd;
	
	private Integer moTotalRecv;
	
	private Integer moHaveSnd;
	
	private Integer moRemained;
	
	private Integer moSndSpd;
	
	private Integer rptTotalRecv;
	
	private Integer rptHaveSnd;
	
	private Integer rptRemained;
	
	private Integer rptSndSpd;
	
	private Integer snderCnt;
	
	private String lastSnderRtm;
	
	private String loginInTm;
	
	private String loginOutTm;
	//修改时间
	private Timestamp updateTime;	
	
	public MmonSpateinfo(){}

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

	public Integer getMtHaveSnd() {
		return mtHaveSnd;
	}

	public void setMtHaveSnd(Integer mtHaveSnd) {
		this.mtHaveSnd = mtHaveSnd;
	}

	public Integer getMtRecvSpd() {
		return mtRecvSpd;
	}

	public void setMtRecvSpd(Integer mtRecvSpd) {
		this.mtRecvSpd = mtRecvSpd;
	}

	public Integer getMoTotalRecv() {
		return moTotalRecv;
	}

	public void setMoTotalRecv(Integer moTotalRecv) {
		this.moTotalRecv = moTotalRecv;
	}

	public Integer getMoHaveSnd() {
		return moHaveSnd;
	}

	public void setMoHaveSnd(Integer moHaveSnd) {
		this.moHaveSnd = moHaveSnd;
	}

	public Integer getMoRemained() {
		return moRemained;
	}

	public void setMoRemained(Integer moRemained) {
		this.moRemained = moRemained;
	}

	public Integer getMoSndSpd() {
		return moSndSpd;
	}

	public void setMoSndSpd(Integer moSndSpd) {
		this.moSndSpd = moSndSpd;
	}

	public Integer getRptTotalRecv() {
		return rptTotalRecv;
	}

	public void setRptTotalRecv(Integer rptTotalRecv) {
		this.rptTotalRecv = rptTotalRecv;
	}

	public Integer getRptHaveSnd() {
		return rptHaveSnd;
	}

	public void setRptHaveSnd(Integer rptHaveSnd) {
		this.rptHaveSnd = rptHaveSnd;
	}

	public Integer getRptRemained() {
		return rptRemained;
	}

	public void setRptRemained(Integer rptRemained) {
		this.rptRemained = rptRemained;
	}

	public Integer getRptSndSpd() {
		return rptSndSpd;
	}

	public void setRptSndSpd(Integer rptSndSpd) {
		this.rptSndSpd = rptSndSpd;
	}

	public Integer getSnderCnt() {
		return snderCnt;
	}

	public void setSnderCnt(Integer snderCnt) {
		this.snderCnt = snderCnt;
	}

	public String getLastSnderRtm() {
		return lastSnderRtm;
	}

	public void setLastSnderRtm(String lastSnderRtm) {
		this.lastSnderRtm = lastSnderRtm;
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

	public Integer getMtRemained() {
		return mtRemained;
	}

	public void setMtRemained(Integer mtRemained) {
		this.mtRemained = mtRemained;
	}	
}
