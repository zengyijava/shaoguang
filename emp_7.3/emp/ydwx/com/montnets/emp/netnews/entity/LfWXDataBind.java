package com.montnets.emp.netnews.entity;


public class LfWXDataBind {

	private Long netId;
	private Long dId;
	public Long getNetId() {
		return netId;
	}
	public void setNetId(Long netId) {
		this.netId = netId;
	}
	public Long getDId() {
		return dId;
	}
	public void setDId(Long dId) {
		this.dId = dId;
	}
	
	public String toString() {
	   	String json="%d";
    	return String.format(json, this.getDId());
	}

	
}
