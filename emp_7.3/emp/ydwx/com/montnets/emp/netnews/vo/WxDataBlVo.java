package com.montnets.emp.netnews.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class WxDataBlVo implements Serializable{
	
	//选项
	private String sname;
	//回复人数
	private Integer hpcount;
	//回复次数
	private Integer hcount;
	//回复人数比例
	private String hpbl;
	//回复比例
	private String  hbl;
	
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	
	public Integer getHpcount() {
		return hpcount;
	}
	public void setHpcount(Integer hpcount) {
		this.hpcount = hpcount;
	}
	public Integer getHcount() {
		return hcount;
	}
	public void setHcount(Integer hcount) {
		this.hcount = hcount;
	}
	public String getHpbl() {
		return hpbl;
	}
	public void setHpbl(String hpbl) {
		this.hpbl = hpbl;
	}
	public String getHbl() {
		return hbl;
	}
	public void setHbl(String hbl) {
		this.hbl = hbl;
	}
	
	
}
