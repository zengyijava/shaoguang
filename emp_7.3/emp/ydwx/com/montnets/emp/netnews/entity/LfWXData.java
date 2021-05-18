package com.montnets.emp.netnews.entity;

import java.sql.Timestamp;

public class LfWXData {

	private Long dId;
	private String code;
	private String name;
	private Long dataTypeId;
	private Integer replySetType;
	private Integer quesType;
	private String quesContent;
	private String colName;
	private Integer colType;
	private Integer colSize;
	private Integer status;
	private Timestamp modifyDate;
	private Timestamp creatDate;
	private String corpCode;
	private Long userId;
	
	
	public Long getDId() {
		return dId;
	}
	public void setDId(Long dId) {
		this.dId = dId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	public Timestamp getCreatDate() {
		return creatDate;
	}
	public void setCreatDate(Timestamp creatDate) {
		this.creatDate = creatDate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Long getDataTypeId() {
		return dataTypeId;
	}
	public void setDataTypeId(Long dataTypeId) {
		this.dataTypeId = dataTypeId;
	}
	public Integer getReplySetType() {
		return replySetType;
	}
	public void setReplySetType(Integer replySetType) {
		this.replySetType = replySetType;
	}
	public Integer getQuesType() {
		return quesType;
	}
	public void setQuesType(Integer quesType) {
		this.quesType = quesType;
	}
	public String getQuesContent() {
		return quesContent;
	}
	public void setQuesContent(String quesContent) {
		this.quesContent = quesContent;
	}
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	public Integer getColType() {
		return colType;
	}
	public void setColType(Integer colType) {
		this.colType = colType;
	}
	public Integer getColSize() {
		return colSize;
	}
	public void setColSize(Integer colSize) {
		this.colSize = colSize;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String toString() {
	   	String json="{did:%d,name:'%s',quesContent:'%s',quesType:%d,colName:'%s'}";
    	return String.format(json, this.getDId(),this.getName(),this.getQuesContent(),this.getQuesType(),this.getColName());
	}
	
}
