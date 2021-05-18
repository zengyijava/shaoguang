
package com.montnets.emp.entity.monitor;

import java.sql.Timestamp;

public class GwClustatus
{

	private Timestamp updtime;

	private Integer id;

	private Integer gwno;

	private Integer runweight;

	private Integer gwtype;

	private Integer gweight;

	private Integer runstatus;

	private Integer prigwno;

	public GwClustatus(){
	} 

	public Timestamp getUpdtime(){

		return updtime;
	}

	public void setUpdtime(Timestamp updtime){

		this.updtime= updtime;

	}

	public Integer getId(){

		return id;
	}

	public void setId(Integer id){

		this.id= id;

	}

	public Integer getGwno(){

		return gwno;
	}

	public void setGwno(Integer gwno){

		this.gwno= gwno;

	}

	public Integer getRunweight(){

		return runweight;
	}

	public void setRunweight(Integer runweight){

		this.runweight= runweight;

	}

	public Integer getGwtype(){

		return gwtype;
	}

	public void setGwtype(Integer gwtype){

		this.gwtype= gwtype;

	}

	public Integer getGweight(){

		return gweight;
	}

	public void setGweight(Integer gweight){

		this.gweight= gweight;

	}

	public Integer getRunstatus(){

		return runstatus;
	}

	public void setRunstatus(Integer runstatus){

		this.runstatus= runstatus;

	}

	public Integer getPrigwno(){

		return prigwno;
	}

	public void setPrigwno(Integer prigwno){

		this.prigwno= prigwno;

	}

}
