					
package com.montnets.emp.entity.appmage;

import java.sql.Timestamp;

public class LfAppmainpagehis
{

	private Integer sendstate;

	private Integer totype;

	private Long userid;

	private Timestamp sendtime;

	private String touser;

	private Long id;

	private Long taskid;

	private String fromuser;

	public LfAppmainpagehis(){
	} 

	public Integer getSendstate(){

		return sendstate;
	}

	public void setSendstate(Integer sendstate){

		this.sendstate= sendstate;

	}

	public Integer getTotype(){

		return totype;
	}

	public void setTotype(Integer totype){

		this.totype= totype;

	}

	public Long getUserid(){

		return userid;
	}

	public void setUserid(Long userid){

		this.userid= userid;

	}

	public Timestamp getSendtime(){

		return sendtime;
	}

	public void setSendtime(Timestamp sendtime){

		this.sendtime= sendtime;

	}

	public String getTouser(){

		return touser;
	}

	public void setTouser(String touser){

		this.touser= touser;

	}

	public Long getId(){

		return id;
	}

	public void setId(Long id){

		this.id= id;

	}

	public Long getTaskid(){

		return taskid;
	}

	public void setTaskid(Long taskid){

		this.taskid= taskid;

	}

	public String getFromuser(){

		return fromuser;
	}

	public void setFromuser(String fromuser){

		this.fromuser= fromuser;

	}

}

					