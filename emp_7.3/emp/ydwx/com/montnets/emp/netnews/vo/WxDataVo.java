package com.montnets.emp.netnews.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class WxDataVo implements Serializable{
	
	//手机号
	private String phone;
	//姓名
	private String name;
	//回复内容
	private String hfcontent;	
	//回复时间
	private String hfdate;
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHfcontent() {
		return hfcontent;
	}
	public void setHfcontent(String hfcontent) {
		this.hfcontent = hfcontent;
	}
	public String getHfdate() {
		return hfdate;
	}
	public void setHfdate(String hfdate) {
		this.hfdate = hfdate;
	}
	


}
