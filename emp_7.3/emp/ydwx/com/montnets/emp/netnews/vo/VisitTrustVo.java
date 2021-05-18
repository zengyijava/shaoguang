/**
 * 
 */
package com.montnets.emp.netnews.vo;

import java.sql.Timestamp;

/**
 * 号码详情对应的实体类
 *
 */
public class VisitTrustVo {
	private String phone;
	private Object username;
	private Object pagename;
	private Object netid;
	private Object netName;
	private Object historytime;

	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Object getUsername() {
		return username;
	}
	public void setUsername(Object username) {
		this.username = username;
	}
	public Object getPagename() {
		return pagename;
	}
	public void setPagename(Object pagename) {
		this.pagename = pagename;
	}
	public Object getNetid() {
		return netid;
	}
	public void setNetid(Object netid) {
		this.netid = netid;
	}
	public Object getNetName() {
		return netName;
	}
	public void setNetName(Object netName) {
		this.netName = netName;
	}
	public Object getHistorytime() {
		return historytime;
	}
	public void setHistorytime(Object historytime) {
		this.historytime = historytime;
	}

}
