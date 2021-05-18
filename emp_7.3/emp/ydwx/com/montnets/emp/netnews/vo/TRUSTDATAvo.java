package com.montnets.emp.netnews.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class TRUSTDATAvo implements Serializable{
	//操作员名称
	private String UNAME = "";
	//机构名称
	private String UDEP = "";

	//任务Mtid
	private Long  mtid = 0L;	
	
	private Long taskid;
	
	//网讯主题
	private String title = "";	
	
	private Integer subState;
	private Integer reState;
	private Integer sendstate;
	
	//是否定时发送  1-是 0-否
	private Integer timerStatus;
	//网讯编号
	private Long  NETID = 0L;		
	//网讯名称
	private String NETNAME = "";		
	//短信消息
	private String NETMSG = "";			
	
	private Long ID;				//自动编号
	private String CODE = "";			//互动名称
	private String NAME = "";			//互动名称
	private String NAMETYPE = "";			//互动类别
	
	private String TABLENAME = "";		//数据表名
	private String colname;
	
	private Timestamp SENDDATE;		//发送时间
	
	private Long spcount;   //发送人数
	private int scount;    //访问人数
	
	private int pcount;   //回复人数
	private int count;    //回复次数
	
	
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
	public String getColname() {
		return colname;
	}
	public void setColname(String colname) {
		this.colname = colname;
	}
	public Long getMtid() {
		return mtid;
	}
	public void setMtid(Long mtid) {
		this.mtid = mtid;
	}
	public String getCODE() {
		return CODE;
	}
	public void setCODE(String cODE) {
		CODE = cODE==null?"":cODE;
	}
	public String getUDEP() {
		return UDEP;
	}
	public void setUDEP(String uDEP) {
		UDEP = uDEP;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public String getNAMETYPE() {
		return NAMETYPE;
	}
	public void setNAMETYPE(String nAMETYPE) {
		NAMETYPE = nAMETYPE==null?"":nAMETYPE;
	}
	public Timestamp getSENDDATE() {
		return SENDDATE;
	}
	public void setSENDDATE(Timestamp sENDDATE) {
		SENDDATE = sENDDATE;
	}
	
	public Long getSpcount() {
		return spcount;
	}
	public void setSpcount(Long spcount) {
		this.spcount = spcount;
	}
	public int getScount() {
		return scount;
	}
	public void setScount(int scount) {
		this.scount = scount;
	}
	public Long getID() {
		return ID;
	}
	public void setID(Long id) {
		ID = id;
	}

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String name) {
		NAME = name==null?"":name;
	}


	public String getTABLENAME() {
		return TABLENAME;
	}


	public void setTABLENAME(String tablename) {
		TABLENAME = tablename;
	}


	public int getPcount() {
		return pcount;
	}


	public void setPcount(int pcount) {
		this.pcount = pcount;
	}


	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}


	@Override
	public String toString() {
	   	String json="{ID:%d,NAME:'%s',UNAME:'%s',TABLENAME:'%s'}";
    	return String.format(json, this.getID(),this.getNAME(),this.getUNAME(),this.getTABLENAME());
	}


	public String getUNAME() {
		return UNAME;
	}


	public void setUNAME(String uname) {
		UNAME = uname;
	}

}
