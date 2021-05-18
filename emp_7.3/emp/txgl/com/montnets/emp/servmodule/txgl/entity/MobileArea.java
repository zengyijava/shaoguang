package com.montnets.emp.servmodule.txgl.entity;

import java.sql.Timestamp;

public class MobileArea implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5937643225347060550L;
	private Long id;
	private String mobile;
	private String areacode;
	private Timestamp createtime;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAreacode() {
		return areacode;
	}
	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}
	public Timestamp getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}
	
	
}
