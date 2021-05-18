package com.montnets.emp.servmodule.txgl.entity;

public class ProvinceCity implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1998155169680003872L;
	private Long id;
	private String province;
	private String city;
	private String areacode;
	private String provincecode;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAreacode() {
		return areacode;
	}
	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}
	public String getProvincecode() {
		return provincecode;
	}
	public void setProvincecode(String provincecode) {
		this.provincecode = provincecode;
	}
	
}
