							
package com.montnets.emp.entity.ydyw;

import java.sql.Timestamp;

public class LfDepTaocan
{

	private Long depid;

	private Long userid;

	private String taocancode;

	private Long contractuser;

	private Long id;

	private Long contractdep;

	private String corpcode;

	public LfDepTaocan(){
	} 

	public Long getDepid(){

		return depid;
	}

	public void setDepid(Long depid){

		this.depid= depid;

	}

	public Long getUserid(){

		return userid;
	}

	public void setUserid(Long userid){

		this.userid= userid;

	}

	public String getTaocancode(){

		return taocancode;
	}

	public void setTaocancode(String taocancode){

		this.taocancode= taocancode;

	}

	public Long getContractuser(){

		return contractuser;
	}

	public void setContractuser(Long contractuser){

		this.contractuser= contractuser;

	}

	public Long getId(){

		return id;
	}

	public void setId(Long id){

		this.id= id;

	}

	public Long getContractdep(){

		return contractdep;
	}

	public void setContractdep(Long contractdep){

		this.contractdep= contractdep;

	}

	public String getCorpcode(){

		return corpcode;
	}

	public void setCorpcode(String corpcode){

		this.corpcode= corpcode;

	}

}

							