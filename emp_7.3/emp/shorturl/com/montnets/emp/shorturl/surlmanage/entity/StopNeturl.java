package com.montnets.emp.shorturl.surlmanage.entity;

public class StopNeturl {
	//id
	private Long id ;
	
	//地址
	private String url;
	
	//企业编号
	private String corpcode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCorpcode() {
		return corpcode;
	}

	public void setCorpcode(String corpcode) {
		this.corpcode = corpcode;
	}
	
	
	
}
