package com.montnets.emp.shorturl.surlmanage.entity;

import java.sql.Timestamp;


public class LfDomainCorp {
	private Long domainId;
	private String  corpCode;
	//创建人员
	private Long createUid;
	//创建时间
	private Timestamp createTm;
	//最后修改人员ID 
	private Long updateUid;
	//最后修改时间
	private Timestamp updateTm;
	//短域名绑定状态  0  有效    -1 无效
	private Integer flag;
	//创建用户CREATE_USER
	private String  createUser;
	
	public Long getDomainId() {
		return domainId;
	}
	public void setDomainId(Long domainId) {
		this.domainId = domainId;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
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
	public Long getUpdateUid() {
		return updateUid;
	}
	public void setUpdateUid(Long updateUid) {
		this.updateUid = updateUid;
	}
	public Timestamp getUpdateTm() {
		return updateTm;
	}
	public void setUpdateTm(Timestamp updateTm) {
		this.updateTm = updateTm;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
}
