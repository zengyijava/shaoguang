package com.montnets.emp.entity.sysuser;

import java.sql.Timestamp;

public class LfBalanceDef
{

	private Timestamp moditime;

	private Long smscount;

	private Long mmscount;

	private Long depid;

	private Timestamp createtime;

	private Long modiuserid;

	private Long id;

	private String corpcode;

	public LfBalanceDef(){
	} 

	public Timestamp getModitime(){

		return moditime;
	}

	public void setModitime(Timestamp moditime){

		this.moditime= moditime;

	}

	public Long getSmscount(){

		return smscount;
	}

	public void setSmscount(Long smscount){

		this.smscount= smscount;

	}

	public Long getMmscount(){

		return mmscount;
	}

	public void setMmscount(Long mmscount){

		this.mmscount= mmscount;

	}

	public Long getDepid(){

		return depid;
	}

	public void setDepid(Long depid){

		this.depid= depid;

	}

	public Timestamp getCreatetime(){

		return createtime;
	}

	public void setCreatetime(Timestamp createtime){

		this.createtime= createtime;

	}

	public Long getModiuserid(){

		return modiuserid;
	}

	public void setModiuserid(Long modiuserid){

		this.modiuserid= modiuserid;

	}

	public Long getId(){

		return id;
	}

	public void setId(Long id){

		this.id= id;

	}

	public String getCorpcode(){

		return corpcode;
	}

	public void setCorpcode(String corpcode){

		this.corpcode= corpcode;

	}

}
