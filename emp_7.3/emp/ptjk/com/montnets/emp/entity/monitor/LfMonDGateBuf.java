package com.montnets.emp.entity.monitor;

import java.sql.Timestamp;

public class LfMonDGateBuf {
	private Integer rptsdwaitbuf;

	private Integer mosdbuf;

	private Integer rptsdbuf;

	private Integer mtupdbuf;

	private Integer mtsdbuf;

	private Timestamp updatetime;

	private Long proceid;

	private Integer mosdwaitbuf;

	private Integer rptrvcnt;

	private Long id;

	private Integer mtsdcnt;

	private Integer mtspd1;

	private Integer morvcnt;

	private Integer mtspd2;

	private Integer morptspd;

	private Integer rptrvbuf;

	private Integer morvbuf;

	private Long gatewayid;

	private Integer mtrvcnt;

	private Integer mtsdwaitbuf;
	
	//告警级别
	private Integer evttype;
	
	// 通道账号名称
	private String		gatename;
	
	// 通道账号
	private String		gateaccount;
	
    //数据库当前入库时间
    private Timestamp dbservtime;
    
	public Integer getRptsdwaitbuf() {
		return rptsdwaitbuf;
	}

	public void setRptsdwaitbuf(Integer rptsdwaitbuf) {
		this.rptsdwaitbuf = rptsdwaitbuf;
	}

	public Integer getMosdbuf() {
		return mosdbuf;
	}

	public void setMosdbuf(Integer mosdbuf) {
		this.mosdbuf = mosdbuf;
	}

	public Integer getRptsdbuf() {
		return rptsdbuf;
	}

	public void setRptsdbuf(Integer rptsdbuf) {
		this.rptsdbuf = rptsdbuf;
	}

	public Integer getMtupdbuf() {
		return mtupdbuf;
	}

	public void setMtupdbuf(Integer mtupdbuf) {
		this.mtupdbuf = mtupdbuf;
	}

	public Integer getMtsdbuf() {
		return mtsdbuf;
	}

	public void setMtsdbuf(Integer mtsdbuf) {
		this.mtsdbuf = mtsdbuf;
	}

	public Timestamp getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

	public Long getProceid() {
		return proceid;
	}

	public void setProceid(Long proceid) {
		this.proceid = proceid;
	}

	public Integer getMosdwaitbuf() {
		return mosdwaitbuf;
	}

	public void setMosdwaitbuf(Integer mosdwaitbuf) {
		this.mosdwaitbuf = mosdwaitbuf;
	}

	public Integer getRptrvcnt() {
		return rptrvcnt;
	}

	public void setRptrvcnt(Integer rptrvcnt) {
		this.rptrvcnt = rptrvcnt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getMtsdcnt() {
		return mtsdcnt;
	}

	public void setMtsdcnt(Integer mtsdcnt) {
		this.mtsdcnt = mtsdcnt;
	}

	public Integer getMtspd1() {
		return mtspd1;
	}

	public void setMtspd1(Integer mtspd1) {
		this.mtspd1 = mtspd1;
	}

	public Integer getMorvcnt() {
		return morvcnt;
	}

	public void setMorvcnt(Integer morvcnt) {
		this.morvcnt = morvcnt;
	}

	public Integer getMtspd2() {
		return mtspd2;
	}

	public void setMtspd2(Integer mtspd2) {
		this.mtspd2 = mtspd2;
	}

	public Integer getMorptspd() {
		return morptspd;
	}

	public void setMorptspd(Integer morptspd) {
		this.morptspd = morptspd;
	}

	public Integer getRptrvbuf() {
		return rptrvbuf;
	}

	public void setRptrvbuf(Integer rptrvbuf) {
		this.rptrvbuf = rptrvbuf;
	}

	public Integer getMorvbuf() {
		return morvbuf;
	}

	public void setMorvbuf(Integer morvbuf) {
		this.morvbuf = morvbuf;
	}

	public Long getGatewayid() {
		return gatewayid;
	}

	public void setGatewayid(Long gatewayid) {
		this.gatewayid = gatewayid;
	}

	public Integer getMtrvcnt() {
		return mtrvcnt;
	}

	public void setMtrvcnt(Integer mtrvcnt) {
		this.mtrvcnt = mtrvcnt;
	}

	public Integer getMtsdwaitbuf() {
		return mtsdwaitbuf;
	}

	public void setMtsdwaitbuf(Integer mtsdwaitbuf) {
		this.mtsdwaitbuf = mtsdwaitbuf;
	}

	public Integer getEvttype() {
		return evttype;
	}

	public void setEvttype(Integer evttype) {
		this.evttype = evttype;
	}

	public String getGatename() {
		return gatename;
	}

	public void setGatename(String gatename) {
		this.gatename = gatename;
	}

	public String getGateaccount() {
		return gateaccount;
	}

	public void setGateaccount(String gateaccount) {
		this.gateaccount = gateaccount;
	}

	public Timestamp getDbservtime()
	{
		return dbservtime;
	}

	public void setDbservtime(Timestamp dbservtime)
	{
		this.dbservtime = dbservtime;
	}
	
}
