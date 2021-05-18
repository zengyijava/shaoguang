package com.montnets.emp.servmodule.txgl.vo;

public class AreaPhNoVo implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2979545845913134818L;
	private String moblie;
	private String areaCode;
	private String province;
	private String servePro;
	private String city;
	private String id;
	//加密ID
	private String keyId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMoblie() {
		return moblie;
	}
	public void setMoblie(String moblie) {
		this.moblie = moblie;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getServePro() {
		return servePro;
	}
	public void setServePro(String servePro) {
		this.servePro = servePro;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getKeyId()
	{
		return keyId;
	}
	public void setKeyId(String keyId)
	{
		this.keyId = keyId;
	}
	
}
