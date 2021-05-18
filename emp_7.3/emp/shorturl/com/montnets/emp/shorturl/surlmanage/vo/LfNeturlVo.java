package com.montnets.emp.shorturl.surlmanage.vo;

import java.sql.Timestamp;

public class LfNeturlVo implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1083784840930893940L;
	
	
	//id主键 
	private Long id;
	
	//源地址 
	private String srcurl;
	
	//连接名称
	private String urlname; 
	
	//内容描述
	private String urlmsg; 
	
	//创建时间
	private Timestamp createtm;
	
	//状态 	-1：禁用 0：启用 （用户侧）
	private Integer urlstate;
	
	//审批状态    -2 禁用  -1:删除   0：未审批, 1：无需审批 ,2：审批通过 （运营商侧） 
	private Integer ispass;
	
	//创建人
	private String name;
	
	//所属机构名
	private String depname;
	
	//审核意见 
	private String remarks;
	
	//预留字段1（禁用意见）
	private String remarks1;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSrcurl() {
		return srcurl;
	}

	public void setSrcurl(String srcurl) {
		this.srcurl = srcurl;
	}

	public String getUrlname() {
		return urlname;
	}

	public void setUrlname(String urlname) {
		this.urlname = urlname;
	}

	public String getUrlmsg() {
		return urlmsg;
	}

	public void setUrlmsg(String urlmsg) {
		this.urlmsg = urlmsg;
	}

	public Timestamp getCreatetm() {
		return createtm;
	}

	public void setCreatetm(Timestamp createtm) {
		this.createtm = createtm;
	}

	public Integer getUrlstate() {
		return urlstate;
	}

	public void setUrlstate(Integer urlstate) {
		this.urlstate = urlstate;
	}

	public Integer getIspass() {
		return ispass;
	}

	public void setIspass(Integer ispass) {
		this.ispass = ispass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepname() {
		return depname;
	}

	public void setDepname(String depname) {
		this.depname = depname;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRemarks1() {
		return remarks1;
	}

	public void setRemarks1(String remarks1) {
		this.remarks1 = remarks1;
	}

	
}
