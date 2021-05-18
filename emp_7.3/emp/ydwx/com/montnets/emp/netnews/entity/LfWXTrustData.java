package com.montnets.emp.netnews.entity;

import java.sql.Timestamp;

/**
 * 业务数据
 * @author Administrator
 *
 */
public class LfWXTrustData {

	private Long id;
	private String name;
	private String tableName;
	private Integer type;
	private Integer colnum;
	private Integer status;
	private Long modifyId;
	private Timestamp modifyDate;
	private Long creatId;
	private Timestamp creatDate;
	private String url;
	private String auditUserId;
	private Timestamp auditDate;
	private String corpCode;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Integer getColnum() {
		return colnum;
	}
	public void setColnum(Integer colnum) {
		this.colnum = colnum;
	}
	public Long getModifyId() {
		return modifyId;
	}
	public void setModifyId(Long modifyId) {
		this.modifyId = modifyId;
	}
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	public Long getCreatId() {
		return creatId;
	}
	public void setCreatId(Long creatId) {
		this.creatId = creatId;
	}
	public Timestamp getCreatDate() {
		return creatDate;
	}
	public void setCreatDate(Timestamp creatDate) {
		this.creatDate = creatDate;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAuditUserId() {
		return auditUserId;
	}
	public void setAuditUserId(String auditUserId) {
		this.auditUserId = auditUserId;
	}
	public Timestamp getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(Timestamp auditDate) {
		this.auditDate = auditDate;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	
	@Override
	public String toString() {
	   	String json="{ID:%d,NAME:'%s',TABLENAME:'%s'}";
    	return String.format(json, this.getId(),this.getName(),this.getTableName());
	}
	
}
