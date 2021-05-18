package com.montnets.emp.entity.wxsystem;

public class LfSysSkin implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3918488836457271765L;
	//主键ID
	private Long sid;
	// 皮肤名称
	private String skinname;
	// 皮肤编码
	private String skincode;
	//皮肤路径
	private String skinsrc;
	//主题编码
	private String themecode;
	
	public String getThemecode() {
		return themecode;
	}
	public void setThemecode(String themecode) {
		this.themecode = themecode;
	}
	public Long getSid() {
		return sid;
	}
	public void setSid(Long sid) {
		this.sid = sid;
	}
	public String getSkinname() {
		return skinname;
	}
	public void setSkinname(String skinname) {
		this.skinname = skinname;
	}
	public String getSkincode() {
		return skincode;
	}
	public void setSkincode(String skincode) {
		this.skincode = skincode;
	}
	public String getSkinsrc() {
		return skinsrc;
	}
	public void setSkinsrc(String skinsrc) {
		this.skinsrc = skinsrc;
	}
	
}
