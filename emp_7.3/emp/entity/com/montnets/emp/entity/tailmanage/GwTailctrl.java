
package com.montnets.emp.entity.tailmanage;

import java.sql.Timestamp;

public class GwTailctrl implements java.io.Serializable
{



	/**
	 * 
	 */
	private static final long serialVersionUID = 5869447046361995009L;

	private Long id;

	private Integer overtailflag;

	private Integer othertailflag;
	
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

	public Integer getOvertailflag() {
		return overtailflag;
	}

	public void setOvertailflag(Integer overtailflag) {
		this.overtailflag = overtailflag;
	}

	public Integer getOthertailflag() {
		return othertailflag;
	}

	public void setOthertailflag(Integer othertailflag) {
		this.othertailflag = othertailflag;
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
