					
package com.montnets.emp.entity.appmage;

import java.sql.Timestamp;

/**
 * 梦网APP用户群组
 * @author Administrator
 *
 */
public class LfAppMwGroup
{

	private Long wgid;

	private String name;

	private Timestamp modifytime;

	private Long aid;

	private String count;

	private Long gid;

	private Timestamp createtime;

	private String corpcode;

	public LfAppMwGroup(){
	} 

	public Long getWgid(){

		return wgid;
	}

	public void setWgid(Long wgid){

		this.wgid= wgid;

	}

	public String getName(){

		return name;
	}

	public void setName(String name){

		this.name= name;

	}

	public Timestamp getModifytime(){

		return modifytime;
	}

	public void setModifytime(Timestamp modifytime){

		this.modifytime= modifytime;

	}

	public Long getAid(){

		return aid;
	}

	public void setAid(Long aid){

		this.aid= aid;

	}

	public String getCount(){

		return count;
	}

	public void setCount(String count){

		this.count= count;

	}

	public Long getGid(){

		return gid;
	}

	public void setGid(Long gid){

		this.gid= gid;

	}

	public Timestamp getCreatetime(){

		return createtime;
	}

	public void setCreatetime(Timestamp createtime){

		this.createtime= createtime;

	}

	public String getCorpcode(){

		return corpcode;
	}

	public void setCorpcode(String corpcode){

		this.corpcode= corpcode;

	}

}

					