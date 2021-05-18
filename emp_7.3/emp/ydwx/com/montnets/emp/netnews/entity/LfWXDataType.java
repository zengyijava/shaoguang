package com.montnets.emp.netnews.entity;

import java.sql.Timestamp;

public class LfWXDataType {

	private Long id;
	private String name;
	private Timestamp updateTime;
	private Timestamp creatDate;
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
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	public Timestamp getCreatDate() {
		return creatDate;
	}
	public void setCreatDate(Timestamp creatDate) {
		this.creatDate = creatDate;
	}
	public String getCorpCode()
	{
		return corpCode;
	}
	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}
	
	public String toString() {
	   	String json="{id:%d,name:'%s'}";
    	return String.format(json, this.getId(),this.getName());
	}
	
}
