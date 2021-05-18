					
package com.montnets.emp.entity.appmage;

import java.sql.Timestamp;

public class LfAppOlGroup
{

	private String gpname;

	private Integer mountcount;

	private Timestamp createtime;

	private Long createuser;

	private Integer apptype;

	private Long gpid;

	public LfAppOlGroup(){
	} 

	public String getGpname(){

		return gpname;
	}

	public void setGpname(String gpname){

		this.gpname= gpname;

	}

	public Integer getMountcount(){

		return mountcount;
	}

	public void setMountcount(Integer mountcount){

		this.mountcount= mountcount;

	}

	public Timestamp getCreatetime(){

		return createtime;
	}

	public void setCreatetime(Timestamp createtime){

		this.createtime= createtime;

	}

	public Long getCreateuser(){

		return createuser;
	}

	public void setCreateuser(Long createuser){

		this.createuser= createuser;

	}

	public Integer getApptype(){

		return apptype;
	}

	public void setApptype(Integer apptype){

		this.apptype= apptype;

	}

	public Long getGpid(){

		return gpid;
	}

	public void setGpid(Long gpid){

		this.gpid= gpid;

	}

}

					