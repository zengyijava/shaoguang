
package com.montnets.emp.entity.tailmanage;

import java.sql.Timestamp;

public class GwTailbind implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8550917748442157751L;

	private Long id;

	private Long tailid;
	
	private Integer tailtype;

	private String buscode;
	
	private String spuserid;
	
	private Timestamp createtime;

	private Timestamp updatetime;

	private String corpcode;
	
	private Long userid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTailid() {
		return tailid;
	}

	public void setTailid(Long tailid) {
		this.tailid = tailid;
	}

	public Integer getTailtype() {
		return tailtype;
	}

	public void setTailtype(Integer tailtype) {
		this.tailtype = tailtype;
	}

	public String getBuscode() {
		return buscode;
	}

	public void setBuscode(String buscode) {
		this.buscode = buscode;
	}

	public String getSpuserid() {
		return spuserid;
	}

	public void setSpuserid(String spuserid) {
		this.spuserid = spuserid;
	}

	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public Timestamp getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

	public String getCorpcode() {
		return corpcode;
	}

	public void setCorpcode(String corpcode) {
		this.corpcode = corpcode;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	

}
