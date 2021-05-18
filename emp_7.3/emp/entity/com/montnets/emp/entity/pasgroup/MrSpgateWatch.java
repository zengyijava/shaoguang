package com.montnets.emp.entity.pasgroup;

import java.sql.Timestamp;
 


/**
 * TableMrSpgateWatch对应的实体类
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:27:31
 * @description 
 */
public class MrSpgateWatch implements java.io.Serializable {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = -878703060957296762L;

	private Long gateId;
	
	private String spgate;

	private String ptcode;
	
	private Long state;
	
	private Timestamp lssendTime;
	
	private Timestamp lsrecvTime;
	
	private Long delayss;
	
	private Long watchNum;
	
	private Long watchSucc;
	
	private Long watchDelay;
	
	private Long numMt;

	private Long numRpt;

	private Long numMo;

	private Long speedMt;

	private Long speedMo;
	
	private Long speedRpt;
	
	private Timestamp updateTime;
	
	private Long dealFlag;
	
	private String errorInfo;
	
	private String usrId;
	
	private Long onlineStatus;

	private Long totalRptSend;
	
	private Long totalMoSend;
	
	private Long haveSendMo;
	
	private Long haveSendRpt;
	
	private Long recvMt;
	
	private Long remainedMt;

	private Long timeRsendMt;
	
	private Long remainedRpt;

	private Long remainedMo;

	private Long recvSpeed;
	
	private Long param1;
	
	private Long param2;
	
	private Long param3;
	
	private Long param4;
	
	private Long param5;
	
	private String param6;
	
	private String param7;
 
	public MrSpgateWatch() {}

	public MrSpgateWatch(Long gateId, String spgate) {
		
		this.gateId = gateId;
		this.spgate = spgate;
	}

	 
	public Long getGateId() {
		return gateId;
	}

	public void setGateId(Long gateId) {
		this.gateId = gateId;
	}

	public String getSpgate() {
		return spgate;
	}

	public void setSpgate(String spgate) {
		this.spgate = spgate;
	}

	public String getPtcode() {
		return ptcode;
	}

	public void setPtcode(String ptcode) {
		this.ptcode = ptcode;
	}

	public Long getState() {
		return state;
	}

	public void setState(Long state) {
		this.state = state;
	}

	public Timestamp getLssendTime() {
		return lssendTime;
	}

	public void setLssendTime(Timestamp lssendTime) {
		this.lssendTime = lssendTime;
	}

	public Timestamp getLsrecvTime() {
		return lsrecvTime;
	}

	public void setLsrecvTime(Timestamp lsrecvTime) {
		this.lsrecvTime = lsrecvTime;
	}

	public Long getDelayss() {
		return delayss;
	}

	public void setDelayss(Long delayss) {
		this.delayss = delayss;
	}

	public Long getWatchNum() {
		return watchNum;
	}

	public void setWatchNum(Long watchNum) {
		this.watchNum = watchNum;
	}

	public Long getWatchSucc() {
		return watchSucc;
	}

	public void setWatchSucc(Long watchSucc) {
		this.watchSucc = watchSucc;
	}

	public Long getWatchDelay() {
		return watchDelay;
	}

	public void setWatchDelay(Long watchDelay) {
		this.watchDelay = watchDelay;
	}

	public Long getNumMt() {
		return numMt;
	}

	public void setNumMt(Long numMt) {
		this.numMt = numMt;
	}

	public Long getNumRpt() {
		return numRpt;
	}

	public void setNumRpt(Long numRpt) {
		this.numRpt = numRpt;
	}

	public Long getNumMo() {
		return numMo;
	}

	public void setNumMo(Long numMo) {
		this.numMo = numMo;
	}

	public Long getSpeedMt() {
		return speedMt;
	}

	public void setSpeedMt(Long speedMt) {
		this.speedMt = speedMt;
	}

	public Long getSpeedMo() {
		return speedMo;
	}

	public void setSpeedMo(Long speedMo) {
		this.speedMo = speedMo;
	}

	public Long getSpeedRpt() {
		return speedRpt;
	}

	public void setSpeedRpt(Long speedRpt) {
		this.speedRpt = speedRpt;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Long getDealFlag() {
		return dealFlag;
	}

	public void setDealFlag(Long dealFlag) {
		this.dealFlag = dealFlag;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	public String getUsrId() {
		return usrId;
	}

	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}

	public Long getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(Long onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public Long getTotalRptSend() {
		return totalRptSend;
	}

	public void setTotalRptSend(Long totalRptSend) {
		this.totalRptSend = totalRptSend;
	}

	public Long getTotalMoSend() {
		return totalMoSend;
	}

	public void setTotalMoSend(Long totalMoSend) {
		this.totalMoSend = totalMoSend;
	}

	public Long getHaveSendMo() {
		return haveSendMo;
	}

	public void setHaveSendMo(Long haveSendMo) {
		this.haveSendMo = haveSendMo;
	}

	public Long getHaveSendRpt() {
		return haveSendRpt;
	}

	public void setHaveSendRpt(Long haveSendRpt) {
		this.haveSendRpt = haveSendRpt;
	}

	public Long getRecvMt() {
		return recvMt;
	}

	public void setRecvMt(Long recvMt) {
		this.recvMt = recvMt;
	}

	public Long getRemainedMt() {
		return remainedMt;
	}

	public void setRemainedMt(Long remainedMt) {
		this.remainedMt = remainedMt;
	}

	public Long getTimeRsendMt() {
		return timeRsendMt;
	}

	public void setTimeRsendMt(Long timeRsendMt) {
		this.timeRsendMt = timeRsendMt;
	}

	public Long getRemainedRpt() {
		return remainedRpt;
	}

	public void setRemainedRpt(Long remainedRpt) {
		this.remainedRpt = remainedRpt;
	}

	public Long getRemainedMo() {
		return remainedMo;
	}

	public void setRemainedMo(Long remainedMo) {
		this.remainedMo = remainedMo;
	}

	public Long getRecvSpeed() {
		return recvSpeed;
	}

	public void setRecvSpeed(Long recvSpeed) {
		this.recvSpeed = recvSpeed;
	}

	public Long getParam1() {
		return param1;
	}

	public void setParam1(Long param1) {
		this.param1 = param1;
	}

	public Long getParam2() {
		return param2;
	}

	public void setParam2(Long param2) {
		this.param2 = param2;
	}

	public Long getParam3() {
		return param3;
	}

	public void setParam3(Long param3) {
		this.param3 = param3;
	}

	public Long getParam4() {
		return param4;
	}

	public void setParam4(Long param4) {
		this.param4 = param4;
	}

	public Long getParam5() {
		return param5;
	}

	public void setParam5(Long param5) {
		this.param5 = param5;
	}

	public String getParam6() {
		return param6;
	}

	public void setParam6(String param6) {
		this.param6 = param6;
	}

	public String getParam7() {
		return param7;
	}

	public void setParam7(String param7) {
		this.param7 = param7;
	}
 
	
}