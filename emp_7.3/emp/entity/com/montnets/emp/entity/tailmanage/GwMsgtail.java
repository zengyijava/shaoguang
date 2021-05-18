
package com.montnets.emp.entity.tailmanage;

import java.sql.Timestamp;

public class GwMsgtail implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7896804909154304526L;

	private Long tailid;
	
	private String tailname;
	
	private String content;
	
	private Timestamp createtime;

	private Timestamp updatetime;

	private String corpcode;
	
	private Long userid;

	public Long getTailid() {
		return tailid;
	}

	public void setTailid(Long tailid) {
		this.tailid = tailid;
	}

	public String getTailname() {
		return tailname;
	}

	public void setTailname(String tailname) {
		this.tailname = tailname;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
