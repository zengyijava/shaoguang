package com.montnets.emp.netnews.entity;

import java.io.Serializable;

public class BaseInfoVO implements Serializable{
	
	private String sendTime;
	private int netid = 0;				//网讯编号
	private String creatid ;
	private String name = "";			//网讯名称
	private String sms = "";
	private int sendcount;
	private int successcount;
	private int falsecount;
	private int visitpeople;
	private int visitcount;
	private  int getfalcount; 
	private int subtotle;  //提交数
	
	@Override
	public String toString() {
		String json="{sendTime:'%s',netid:%d,creatid:'%s',name:'%s',sms:'%s',sendcount:%d,successcount:%d,falsecount:%d,visitpeople:%d,visitcount:%d,subtotle:%d,getfalcount:%d}";
		return String.format(json, this.getSendTime(), this.getNetid(),this.getCreatid(),this.getName(),this.getSms(),this.getSendcount(),this.getSuccesscount(),this.getFalsecount(),this.getVisitpeople(),this.getVisitcount(),this.getSubtotle(),this.getGetfalcount());
	}
	public BaseInfoVO( String sendTime, int netid, String creatid ,String name, String sms, int sendcount, int successcount,
			int falsecount, int visitpeople, int visitcount,int subtotle) {
		super();
		this.sendTime = sendTime;
		this.netid = netid;
		this.creatid = creatid;
		this.name = name;
		this.sms = sms;
		this.sendcount = sendcount;
		this.successcount = successcount;
		this.falsecount = falsecount;
		this.visitpeople = visitpeople;
		this.visitcount = visitcount;
		this.subtotle = subtotle;
	}
	public BaseInfoVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getNetid() {
		return netid;
	}
	public void setNetid(int netid) {
		this.netid = netid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSendcount() {
		return sendcount;
	}
	public void setSendcount(int sendcount) {
		this.sendcount = sendcount;
	}
	public int getSuccesscount() {
		return successcount;
	}
	public void setSuccesscount(int successcount) {
		this.successcount = successcount;
	}
	public int getFalsecount() {
		return falsecount;
	}
	public void setFalsecount(int falsecount) {
		this.falsecount = falsecount;
	}
	public int getVisitpeople() {
		return visitpeople;
	}
	public void setVisitpeople(int visitpeople) {
		this.visitpeople = visitpeople;
	}
	public int getVisitcount() {
		return visitcount;
	}
	public void setVisitcount(int visitcount) {
		this.visitcount = visitcount;
	}
	public String getSms() {
		return sms;
	}
	public void setSms(String sms) {
		this.sms = sms;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public int getSubtotle() {
		return subtotle;
	}
	public void setSubtotle(int subtotle) {
		this.subtotle = subtotle;
	}
	public int getGetfalcount() {
		return getfalcount;
	}
	public void setGetfalcount(int getfalcount) {
		this.getfalcount = getfalcount;
	}
	public String getCreatid() {
		return creatid;
	}
	public void setCreatid(String creatid) {
		this.creatid = creatid;
	}
}
