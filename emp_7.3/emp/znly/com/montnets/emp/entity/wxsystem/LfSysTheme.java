package com.montnets.emp.entity.wxsystem;

public class LfSysTheme implements java.io.Serializable{

	private static final long serialVersionUID = -3918488836457271765L;
	//主键ID
	private Long tid;
	// 皮肤名称
	private String themename;
	// 皮肤编码
	private String themecode;
	//皮肤路径
	private String themesrc;
	
	public String getThemecode() {
		return themecode;
	}
	public void setThemecode(String themecode) {
		this.themecode = themecode;
	}
	public Long getTid() {
		return tid;
	}
	public void setTid(Long tid) {
		this.tid = tid;
	}
	public String getThemename() {
		return themename;
	}
	public void setThemename(String themename) {
		this.themename = themename;
	}
	public String getThemesrc() {
		return themesrc;
	}
	public void setThemesrc(String themesrc) {
		this.themesrc = themesrc;
	}
	
	
}
