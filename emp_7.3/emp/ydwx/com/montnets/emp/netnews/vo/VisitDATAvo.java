package com.montnets.emp.netnews.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class VisitDATAvo implements Serializable{
	private Long ID = 0L;					//自动编号
	private Long  NETID = 0L;				//网讯编号
	private String NETNAME = "";			//网讯名称
	private String NETMSG = "";				//短信消息/网讯消息
	
	private String TABLENAME = "lf_wx_baseinfo";		//数据表名
	private String UNAME = "";			//操作员
	private String UDEP = "";			//隶属机构
	
	private Long mtid;
	private Long taskid;
	
	private Timestamp CREATDATE;		//创建时间/发送时间
	private Timestamp TIMEOUT;			//其他：过期时间/有效时间
	
	private Long visitsendcount;		//接收号码数
	
	private int visitsucc;   //访问成功数
	private int visitfail;    //访问失败数
	private int visitpelple;	//访问人数
	private int visitcount;		//访问次数
	

	private Integer subState;
	private Integer reState;
	private Integer sendstate;
	
	//是否定时发送  1-是 0-否
	private Integer timerStatus;
	
	
	
	public Long getTaskid() {
		return taskid;
	}
	public void setTaskid(Long taskid) {
		this.taskid = taskid;
	}
	public Integer getSubState() {
		return subState;
	}
	public void setSubState(Integer subState) {
		this.subState = subState;
	}
	public Integer getReState() {
		return reState;
	}
	public void setReState(Integer reState) {
		this.reState = reState;
	}
	public Integer getSendstate() {
		return sendstate;
	}
	public void setSendstate(Integer sendstate) {
		this.sendstate = sendstate;
	}
	public Integer getTimerStatus() {
		return timerStatus;
	}
	public void setTimerStatus(Integer timerStatus) {
		this.timerStatus = timerStatus;
	}
	public Long getMtid() {
		return mtid;
	}
	public void setMtid(Long mtid) {
		this.mtid = mtid;
	}
	public String getUDEP() {
		return UDEP;
	}
	public void setUDEP(String uDEP) {
		UDEP = uDEP;
	}
	public Timestamp getTIMEOUT() {
		return TIMEOUT;
	}
	public void setTIMEOUT(Timestamp tIMEOUT) {
		TIMEOUT = tIMEOUT;
	}
	public Timestamp getCREATDATE() {
		return CREATDATE;
	}
	public void setCREATDATE(Timestamp cREATDATE) {
		CREATDATE = cREATDATE;
	}
	public Long getID() {
		return ID;
	}
	public void setID(Long iD) {
		ID = iD;
	}
	public Long getNETID() {
		return NETID;
	}
	public void setNETID(Long nETID) {
		NETID = nETID;
	}
	public String getNETNAME() {
		return NETNAME;
	}
	public void setNETNAME(String nETNAME) {
		NETNAME = nETNAME;
	}
	public String getNETMSG() {
		return NETMSG;
	}
	public void setNETMSG(String nETMSG) {
		NETMSG = nETMSG;
	}
	public String getTABLENAME() {
		return TABLENAME;
	}
	
	public Long getVisitsendcount() {
		return visitsendcount;
	}
	public void setVisitsendcount(Long visitsendcount) {
		this.visitsendcount = visitsendcount;
	}
	public int getVisitsucc() {
		return visitsucc;
	}
	public void setVisitsucc(int visitsucc) {
		this.visitsucc = visitsucc;
	}
	public int getVisitfail() {
		return visitfail;
	}
	public void setVisitfail(int visitfail) {
		this.visitfail = visitfail;
	}
	public int getVisitpelple() {
		return visitpelple;
	}
	public void setVisitpelple(int visitpelple) {
		this.visitpelple = visitpelple;
	}
	public int getVisitcount() {
		return visitcount;
	}
	public void setVisitcount(int visitcount) {
		this.visitcount = visitcount;
	}
	public String getUNAME() {
		return UNAME;
	}
	public void setUNAME(String uNAME) {
		UNAME = uNAME;
	}
}
