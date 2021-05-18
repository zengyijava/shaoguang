package com.montnets.emp.sysuser.util;


public class RequestStr  {
	//接口业务编码
	private String bcode;
	//应用系统编号
	private String apid;
	//应用密钥
	private String cert;
	//请求类型
	private String tpe;
	public String getBcode() {
		return bcode;
	}
	public void setBcode(String bcode) {
		this.bcode = bcode;
	}
	public String getApid() {
		return apid;
	}
	public void setApid(String apid) {
		this.apid = apid;
	}
	public String getCert() {
		return cert;
	}
	public void setCert(String cert) {
		this.cert = cert;
	}
	public String getTpe() {
		return tpe;
	}
	public void setTpe(String tpe) {
		this.tpe = tpe;
	}

}
