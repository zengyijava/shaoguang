package com.montnets.emp.entity.tailmanage;

public class GwTdcmd {


	//需要加入黑名单的业务类型    PB_LIST_BLACK表SVRTYPE字段
	private String pbsvrtype;
	//上行退订指令的企业ID   0表示针对所有企业
	private Integer tdecid;
	//指令匹配类型：0精确匹配、1模糊匹配(信息内容包含即可)都不区分大小写
	private Integer matchtype;
	//	操作类型：0 退订、1解除
	private Integer optype;
	//上行退订指令的通道号  空表示所有通道有效
	private String tdspnumber;
	//需要加入黑名单的通道号	PB_LIST_BLACK表SPNUMBER字段
	private String pbspnumber;
	//需要加入黑名单的企业ID  PB_LIST_BLACK表CORPCODE字段
	private String pbcropcode;
	//指令状态，0 启用、1禁用
	private Integer status;
	//退订指令
	private String tdcmd;
	//需要加入黑名单的SP帐号  PB_LIST_BLACK表USERID字段
	private String pbuserid;
	//自增ID，主键
	private Integer id;
	//上行退订指令的SP帐号  "000000"对所有SP帐号有效
	private String tduserid;
	//回复多少次后加入黑名单
	private Integer tdtimes;

	public GwTdcmd(){
	} 

	public String getPbsvrtype(){

		return pbsvrtype;
	}

	public void setPbsvrtype(String pbsvrtype){

		this.pbsvrtype= pbsvrtype;

	}

	public Integer getTdecid(){

		return tdecid;
	}

	public void setTdecid(Integer tdecid){

		this.tdecid= tdecid;

	}

	public Integer getMatchtype(){

		return matchtype;
	}

	public void setMatchtype(Integer matchtype){

		this.matchtype= matchtype;

	}

	public Integer getOptype(){

		return optype;
	}

	public void setOptype(Integer optype){

		this.optype= optype;

	}

	public String getTdspnumber(){

		return tdspnumber;
	}

	public void setTdspnumber(String tdspnumber){

		this.tdspnumber= tdspnumber;

	}

	public String getPbspnumber(){

		return pbspnumber;
	}

	public void setPbspnumber(String pbspnumber){

		this.pbspnumber= pbspnumber;

	}

	public String getPbcropcode(){

		return pbcropcode;
	}

	public void setPbcropcode(String pbcropcode){

		this.pbcropcode= pbcropcode;

	}

	public Integer getStatus(){

		return status;
	}

	public void setStatus(Integer status){

		this.status= status;

	}

	public String getTdcmd(){

		return tdcmd;
	}

	public void setTdcmd(String tdcmd){

		this.tdcmd= tdcmd;

	}

	public String getPbuserid(){

		return pbuserid;
	}

	public void setPbuserid(String pbuserid){

		this.pbuserid= pbuserid;

	}

	public Integer getId(){

		return id;
	}

	public void setId(Integer id){

		this.id= id;

	}

	public String getTduserid(){

		return tduserid;
	}

	public void setTduserid(String tduserid){

		this.tduserid= tduserid;

	}

	public Integer getTdtimes(){

		return tdtimes;
	}

	public void setTdtimes(Integer tdtimes){

		this.tdtimes= tdtimes;

	}

}
