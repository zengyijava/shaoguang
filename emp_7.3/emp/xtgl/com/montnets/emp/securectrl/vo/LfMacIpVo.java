package com.montnets.emp.securectrl.vo;

import java.sql.Timestamp;

public class LfMacIpVo implements java.io.Serializable
{
	/**
	 * mac-ip  
	 */
	private static final long serialVersionUID = -4943327546486340200L;
    //主键
	private Long lmiid;
	//用户guid
	private Long guid;
	//用户名称
	private String userName;
    //用户名称
	private String name;
    //机构名称
	private String depName;
	//ip
	private String ipaddr;
	//mac
	private String macaddr;
	
	private String creatorName;
	//创建时间
	private Timestamp creatTime;
	//类型
	private Integer type;
	
	private Integer dtpwd;
	
	public Long getLmiid() {
		return lmiid;
	}

	public void setLmiid(Long lmiid) {
		this.lmiid = lmiid;
	}
	
	public Long getGuid() {
		return guid;
	}

	public void setGuid(Long guid) {
		this.guid = guid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepName()
	{
		return depName;
	}

	public void setDepName(String depName)
	{
		this.depName = depName;
	}

	public String getName()
	{
		return name;
	}

	public void setrName(String name)
	{
		this.name = name;
	}

	public String getIpaddr() {
		return ipaddr;
	}

	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}

	public String getMacaddr() {
		return macaddr;
	}

	public void setMacaddr(String macaddr) {
		this.macaddr = macaddr;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Timestamp getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Timestamp creatTime) {
		this.creatTime = creatTime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getDtpwd() {
		return dtpwd;
	}

	public void setDtpwd(Integer dtpwd) {
		this.dtpwd = dtpwd;
	}	
}
