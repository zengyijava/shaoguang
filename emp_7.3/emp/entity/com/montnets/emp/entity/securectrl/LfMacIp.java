package com.montnets.emp.entity.securectrl;

import java.sql.Timestamp;

/**
 * Mac地址与IP地址绑定表
 * @author Administrator
 *
 */
public class LfMacIp implements java.io.Serializable
{  
	private static final long serialVersionUID = -453720090586239004L;
	//主键  标识列
	private Long lmiid;
	//操作员ID
	private Long guid;
	//mac地址
	private String macaddr;
	//IP地址
	private String ipaddr;
	//绑定类型(无效)
	private Integer type;
	//绑定人(创建人)
	private String creatorName;
	//绑定时间(创建时间)
	private Timestamp creatTime;
	//是否启用动态口令(0是不启用,1是启用)
	private Integer dtpwd = 0;
	
	public Integer getDtpwd() {
		return dtpwd;
	}
	public void setDtpwd(Integer dtpwd) {
		this.dtpwd = dtpwd;
	}
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
	public String getMacaddr() {
		return macaddr;
	}
	public void setMacaddr(String macaddr) {
		this.macaddr = macaddr;
	}
	public String getIpaddr() {
		return ipaddr;
	}
	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
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
	
}