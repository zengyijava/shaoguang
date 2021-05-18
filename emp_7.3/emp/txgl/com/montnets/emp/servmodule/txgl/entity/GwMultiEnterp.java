package com.montnets.emp.servmodule.txgl.entity;

import java.sql.Timestamp;

public class GwMultiEnterp
{
	//已匹配的接口个数
	private Integer matchcnt;
	//方法类型：例如：cstd/jd0001,主动推送给用户时，此值为空(‘’)
	private String funtype;
	//订阅接口数量
	private Integer bookcnt;
	//记录最后修改时间
	private Timestamp modiytm;
	//回应格式回应格式，对应4种格式的回应，位表示0:未知,1：XML,2：JSON,4:  URLENCODE
	private Integer respfmt;
	//状态:企业接口状态,0：禁用,1：启用
	private Integer status;
	//记录创建时间
	private Timestamp createtm;
	//企业ID 主键
	private Integer ecid;
	
	private Integer reqfmt;

	public Integer getReqfmt() {
		return reqfmt;
	}


	public void setReqfmt(Integer reqfmt) {
		this.reqfmt = reqfmt;
	}


	public GwMultiEnterp(){
	} 

	
	public Integer getMatchcnt(){

		return matchcnt;
	}

	public void setMatchcnt(Integer matchcnt){

		this.matchcnt= matchcnt;

	}

	public String getFuntype(){

		return funtype;
	}

	public void setFuntype(String funtype){

		this.funtype= funtype;

	}

	public Integer getBookcnt(){

		return bookcnt;
	}

	public void setBookcnt(Integer bookcnt){

		this.bookcnt= bookcnt;

	}

	public Timestamp getModiytm(){

		return modiytm;
	}

	public void setModiytm(Timestamp modiytm){

		this.modiytm= modiytm;

	}

	public Integer getRespfmt(){

		return respfmt;
	}

	public void setRespfmt(Integer respfmt){

		this.respfmt= respfmt;

	}

	public Integer getStatus(){

		return status;
	}

	public void setStatus(Integer status){

		this.status= status;

	}

	public Timestamp getCreatetm(){

		return createtm;
	}

	public void setCreatetm(Timestamp createtm){

		this.createtm= createtm;

	}

	public Integer getEcid(){

		return ecid;
	}

	public void setEcid(Integer ecid){

		this.ecid= ecid;

	}

}

