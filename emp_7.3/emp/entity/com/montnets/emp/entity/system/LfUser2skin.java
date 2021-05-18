package com.montnets.emp.entity.system;

public class LfUser2skin implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3918488836457271765L;

	//主键ID
	private Long usid;
	//皮肤id
	private Long sid;
	//操作员id
	private Long userid;
	//皮肤编码
	private String skincode;
	//主题编码
	private String themecode;
	//主题使用状态（取值1-使用中,0-未使用）
	private Integer themeuse;
	
	//监控是否启用告警声音提示，0-开启；1-不开启
	private Integer monvoice = 0;
	

	public Integer getMonvoice()
	{
		return monvoice;
	}
	public void setMonvoice(Integer monvoice)
	{
		this.monvoice = monvoice;
	}
	public Integer getThemeuse() {
		return themeuse;
	}
	public void setThemeuse(Integer themeuse) {
		this.themeuse = themeuse;
	}
	public String getThemecode() {
		return themecode;
	}
	public void setThemecode(String themecode) {
		this.themecode = themecode;
	}
	
	public Long getUsid() {
		return usid;
	}
	public void setUsid(Long usid) {
		this.usid = usid;
	}
	public Long getSid() {
		return sid;
	}
	public void setSid(Long sid) {
		this.sid = sid;
	}
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public String getSkincode() {
		return skincode;
	}
	public void setSkincode(String skincode) {
		this.skincode = skincode;
	}
	
}
