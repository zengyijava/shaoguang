package com.montnets.emp.entity.monitorsys;

import java.sql.Timestamp;

public class MmonBufinfo implements java.io.Serializable{

	private static final long serialVersionUID = -2714646504787057063L;

	private String ptcode;
	
	private Integer endCnt;
	
	private Integer moTotalRecv;
	
	private Integer mtTotalSnd;
	
	private Integer wrMoBuf;
	
	private Integer upDmoBuf;
	
	private Integer wrRptBuf;
	
    private Integer updRptBuf;
	
	private Integer endRspBuf;
	
	private String smTsndBuf;
	
	private Integer nmTsndBuf;
	
	private Integer mtWaitBuf;
	
	private Integer preCnt;
	
	private Integer mtTotalRecv;
	
	private Integer moTotalSnd;
	
	private Integer wrMttaskBuf;
	
	private Integer wrMtTmBuf;
	
	private Integer wrMtvfyBuf;
	
	private Integer wrMtLvlBuf;	
	
	private Integer preRspBuf;
	
	private Integer preRspTmpBuf;
	
	private Integer moSndBuf;
	
	private Integer rptSndBuf;
	
	private Integer moRptWaitBuf;
	
	private Integer logFileNum;
	
	private Integer logBuf;
	
	private Integer recvBuf;
	private Integer reSndBuf;
	private Integer suppSndBuf;
	
	private Integer monLogBuf;
	
	private Timestamp updateTime;
	
	public MmonBufinfo(){}

	public String getPtcode() {
		return ptcode;
	}

	public void setPtcode(String ptcode) {
		this.ptcode = ptcode;
	}

	public Integer getEndCnt() {
		return endCnt;
	}

	public void setEndCnt(Integer endCnt) {
		this.endCnt = endCnt;
	}

	public Integer getMoTotalRecv() {
		return moTotalRecv;
	}

	public void setMoTotalRecv(Integer moTotalRecv) {
		this.moTotalRecv = moTotalRecv;
	}

	public Integer getMtTotalSnd() {
		return mtTotalSnd;
	}

	public void setMtTotalSnd(Integer mtTotalSnd) {
		this.mtTotalSnd = mtTotalSnd;
	}

	public Integer getWrMoBuf() {
		return wrMoBuf;
	}

	public void setWrMoBuf(Integer wrMoBuf) {
		this.wrMoBuf = wrMoBuf;
	}

	public Integer getUpDmoBuf() {
		return upDmoBuf;
	}

	public void setUpDmoBuf(Integer upDmoBuf) {
		this.upDmoBuf = upDmoBuf;
	}

	public Integer getWrRptBuf() {
		return wrRptBuf;
	}

	public void setWrRptBuf(Integer wrRptBuf) {
		this.wrRptBuf = wrRptBuf;
	}

	public Integer getEndRspBuf() {
		return endRspBuf;
	}

	public void setEndRspBuf(Integer endRspBuf) {
		this.endRspBuf = endRspBuf;
	}

	public String getSmTsndBuf() {
		return smTsndBuf;
	}

	public void setSmTsndBuf(String smTsndBuf) {
		this.smTsndBuf = smTsndBuf;
	}

	public Integer getNmTsndBuf() {
		return nmTsndBuf;
	}

	public void setNmTsndBuf(Integer nmTsndBuf) {
		this.nmTsndBuf = nmTsndBuf;
	}

	public Integer getMtWaitBuf() {
		return mtWaitBuf;
	}

	public void setMtWaitBuf(Integer mtWaitBuf) {
		this.mtWaitBuf = mtWaitBuf;
	}

	public Integer getPreCnt() {
		return preCnt;
	}

	public void setPreCnt(Integer preCnt) {
		this.preCnt = preCnt;
	}

	public Integer getMtTotalRecv() {
		return mtTotalRecv;
	}

	public void setMtTotalRecv(Integer mtTotalRecv) {
		this.mtTotalRecv = mtTotalRecv;
	}

	public Integer getMoTotalSnd() {
		return moTotalSnd;
	}

	public void setMoTotalSnd(Integer moTotalSnd) {
		this.moTotalSnd = moTotalSnd;
	}

	public Integer getWrMttaskBuf() {
		return wrMttaskBuf;
	}

	public void setWrMttaskBuf(Integer wrMttaskBuf) {
		this.wrMttaskBuf = wrMttaskBuf;
	}

	public Integer getWrMtvfyBuf() {
		return wrMtvfyBuf;
	}

	public void setWrMtvfyBuf(Integer wrMtvfyBuf) {
		this.wrMtvfyBuf = wrMtvfyBuf;
	}

	public Integer getWrMtLvlBuf() {
		return wrMtLvlBuf;
	}

	public void setWrMtLvlBuf(Integer wrMtLvlBuf) {
		this.wrMtLvlBuf = wrMtLvlBuf;
	}

	public Integer getPreRspBuf() {
		return preRspBuf;
	}

	public void setPreRspBuf(Integer preRspBuf) {
		this.preRspBuf = preRspBuf;
	}

	public Integer getPreRspTmpBuf() {
		return preRspTmpBuf;
	}

	public void setPreRspTmpBuf(Integer preRspTmpBuf) {
		this.preRspTmpBuf = preRspTmpBuf;
	}

	public Integer getMoSndBuf() {
		return moSndBuf;
	}

	public void setMoSndBuf(Integer moSndBuf) {
		this.moSndBuf = moSndBuf;
	}

	public Integer getRptSndBuf() {
		return rptSndBuf;
	}

	public void setRptSndBuf(Integer rptSndBuf) {
		this.rptSndBuf = rptSndBuf;
	}

	public Integer getMoRptWaitBuf() {
		return moRptWaitBuf;
	}

	public void setMoRptWaitBuf(Integer moRptWaitBuf) {
		this.moRptWaitBuf = moRptWaitBuf;
	}

	public Integer getLogFileNum() {
		return logFileNum;
	}

	public void setLogFileNum(Integer logFileNum) {
		this.logFileNum = logFileNum;
	}

	public Integer getLogBuf() {
		return logBuf;
	}

	public void setLogBuf(Integer logBuf) {
		this.logBuf = logBuf;
	}

	public Integer getRecvBuf() {
		return recvBuf;
	}

	public void setRecvBuf(Integer recvBuf) {
		this.recvBuf = recvBuf;
	}

	public Integer getSuppSndBuf() {
		return suppSndBuf;
	}

	public void setSuppSndBuf(Integer suppSndBuf) {
		this.suppSndBuf = suppSndBuf;
	}

	public Integer getMonLogBuf() {
		return monLogBuf;
	}

	public void setMonLogBuf(Integer monLogBuf) {
		this.monLogBuf = monLogBuf;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getUpdRptBuf() {
		return updRptBuf;
	}

	public void setUpdRptBuf(Integer updRptBuf) {
		this.updRptBuf = updRptBuf;
	}

	public Integer getWrMtTmBuf() {
		return wrMtTmBuf;
	}

	public void setWrMtTmBuf(Integer wrMtTmBuf) {
		this.wrMtTmBuf = wrMtTmBuf;
	}

	public Integer getReSndBuf() {
		return reSndBuf;
	}

	public void setReSndBuf(Integer reSndBuf) {
		this.reSndBuf = reSndBuf;
	}	
}
