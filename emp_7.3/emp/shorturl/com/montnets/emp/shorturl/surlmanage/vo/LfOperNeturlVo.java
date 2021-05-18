package com.montnets.emp.shorturl.surlmanage.vo;

import java.sql.Timestamp;

public class LfOperNeturlVo {
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
	
	//创建人
	private Integer createuid ;
	
	//创建人名
	private  String createuser;
	
	//企业编码 
	private String corpcode;
	
	//企业名称
	private String corpname;
	
	//创建时间
	private Timestamp createtm;
	
	//审批状态    -2 禁用  -1:删除   0：未审批, 1：无需审批 ,2：审批通过 （运营商侧），3审批不通过
	private Integer ispass;
	
	//状态 	-1：禁用 0：启用 （用户侧）
	private Integer urlstate;
	//审核人id
	private Integer audituid; 
	
	//审核意见 
	private String remarks;
	
	//禁用意见 
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

	public Integer getCreateuid() {
		return createuid;
	}

	public void setCreateuid(Integer createuid) {
		this.createuid = createuid;
	}

	public String getCreateuser() {
		return createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	public String getCorpcode() {
		return corpcode;
	}

	public void setCorpcode(String corpcode) {
		this.corpcode = corpcode;
	}

	public String getCorpname() {
		return corpname;
	}

	public void setCorpname(String corpname) {
		this.corpname = corpname;
	}

	public Timestamp getCreatetm() {
		return createtm;
	}

	public void setCreatetm(Timestamp createtm) {
		this.createtm = createtm;
	}

	public Integer getIspass() {
		return ispass;
	}

	public void setIspass(Integer ispass) {
		this.ispass = ispass;
	}

	public Integer getAudituid() {
		return audituid;
	}

	public void setAudituid(Integer audituid) {
		this.audituid = audituid;
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

	public Integer getUrlstate() {
		return urlstate;
	}

	public void setUrlstate(Integer urlstate) {
		this.urlstate = urlstate;
	}
	
	
	
}
