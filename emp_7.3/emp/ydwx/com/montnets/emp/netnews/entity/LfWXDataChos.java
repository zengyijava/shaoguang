package com.montnets.emp.netnews.entity;


public class LfWXDataChos {

	private Long dId;
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getDId() {
		return dId;
	}
	public void setDId(Long dId) {
		this.dId = dId;
	}

	public String toString() {
	   	String json="{dId:%d,name:'%s'}";
    	return String.format(json, this.getDId(),this.getName());
	}
	
}
