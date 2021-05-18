package com.montnets.emp.rms.wbs.model;

import java.io.Serializable;

public class SvcCont implements Serializable{
	
	private static final long serialVersionUID = 2088748554619320433L;
	private String spisuncm;// 运营商
	private String userid;// sp账号
	private String iymd;// 年月日
	private String degree;// 档位
	private String icount;// 提交号码数量
	private String rsucc;// 发送成功数量
	private String rfail;// 发送失败数量
	private String flag;// 数据请求结束标记
	private String param1;// 拓展参数1
	private String param2;// 拓展参数2
	private String param3;// 拓展参数3
	private String param4;// 拓展参数4
    private String corpCode;//企业编码
    
    // 业务属性
    private int resultCode;
    private String resultMsg;
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	public SvcCont(){
		
	}
	public String getSpisuncm() {
		return spisuncm;
	}
	public void setSpisuncm(String spisuncm) {
		this.spisuncm = spisuncm;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getIymd() {
		return iymd;
	}
	public void setIymd(String iymd) {
		this.iymd = iymd;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getIcount() {
		return icount;
	}
	public void setIcount(String icount) {
		this.icount = icount;
	}
	public String getRsucc() {
		return rsucc;
	}
	public void setRsucc(String rsucc) {
		this.rsucc = rsucc;
	}
	public String getRfail() {
		return rfail;
	}
	public void setRfail(String rfail) {
		this.rfail = rfail;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getParam1() {
		return param1;
	}
	public void setParam1(String param1) {
		this.param1 = param1;
	}
	public String getParam2() {
		return param2;
	}
	public void setParam2(String param2) {
		this.param2 = param2;
	}
	public String getParam3() {
		return param3;
	}
	public void setParam3(String param3) {
		this.param3 = param3;
	}
	public String getParam4() {
		return param4;
	}
	public void setParam4(String param4) {
		this.param4 = param4;
	}
	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	@Override
	public String toString() {
		return "SvcCont [spisuncm=" + spisuncm + ", userid=" + userid + ", iymd=" + iymd + ", degree=" + degree
				+ ", icount=" + icount + ", rsucc=" + rsucc + ", rfail=" + rfail + ", flag=" + flag + ", param1="
				+ param1 + ", param2=" + param2 + ", param3=" + param3 + ", param4=" + param4 + ", corpCode=" + corpCode
				+ ", resultCode=" + resultCode + ", resultMsg=" + resultMsg + "]";
	}
	
	
}
