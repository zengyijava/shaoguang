package com.montnets.emp.sysuser.util;


public class ReturnReq  {
	//接口业务编码
	private String bcode;
	//应用系统编号
	private String apid;
	//应用密钥
	private String cert;
	//错误编码
	private String resultCode;
	//错误信息
	private String resultMsg;
	//处理状态
	private String sta;
	//错误描述
	private String err;
	//流水号
	private String ser;
	//是否有流水号
	private boolean haveSer;
	
	public boolean isHaveSer() {
		return haveSer;
	}
	public void setHaveSer(boolean haveSer) {
		this.haveSer = haveSer;
	}
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
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	public String getSta() {
		return sta;
	}
	public void setSta(String sta) {
		this.sta = sta;
	}
	public String getErr() {
		return err;
	}
	public void setErr(String err) {
		this.err = err;
	}
	public String getSer() {
		return ser;
	}
	public void setSer(String ser) {
		this.ser = ser;
	}

	
	
	
	

}
