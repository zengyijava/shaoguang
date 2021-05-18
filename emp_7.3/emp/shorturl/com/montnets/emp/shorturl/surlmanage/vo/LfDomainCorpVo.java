package com.montnets.emp.shorturl.surlmanage.vo;

import java.sql.Timestamp;

public class LfDomainCorpVo {

	private static final long serialVersionUID = 1083784840930893941L;
	
	
	//id主键 
	private Long corpID;
	
	//企业编码
	private String corpCode;
	
	//企业名称
	private String corpName;
	
	//绑定短域名id
	private Long id;
	
	//绑定短域名
	private String domain;
	
	//短域名绑定状态
	private Integer flag;
	
	//创建人
	private Long createUid;
	
	//创建用户
	private String createUser;
	
	//创建时间
	private Timestamp createTm;
	
	//修改时间
	private Timestamp updateTm;

	public Long getCorpID() {
		return corpID;
	}

	public void setCorpID(Long corpID) {
		this.corpID = corpID;
	}

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Long getCreateUid() {
		return createUid;
	}

	public void setCreateUid(Long createUid) {
		this.createUid = createUid;
	}

	public Timestamp getCreateTm() {
		return createTm;
	}

	public void setCreateTm(Timestamp createTm) {
		this.createTm = createTm;
	}

	public Timestamp getUpdateTm() {
		return updateTm;
	}

	public void setUpdateTm(Timestamp updateTm) {
		this.updateTm = updateTm;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

}
