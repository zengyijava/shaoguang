package com.montnets.emp.entity.birthwish;

import java.sql.Timestamp;

public class LfBirthdaySetup implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4392716739218897478L;
	private Long id;
	//操作员id
	private Long userId;
	//祝福语内容
	private String msg;
	//发送时间
	private Timestamp sendTime;
	//是否加尊称(1是；2否)
	private Integer isAddName;
	//尊称
	private String addName;
	//发送账号
	private String spUser;
	//是否启用(1是；2否)
	private Integer isUse;
	//祝福类型(1员工生日祝福；2客户生日祝福)
	private Integer type;
	//企业编码 
	private String corpCode;
	//业务编码
	private String buscode;
	//标题
	private String title;
	//是否签名，1是，2否
	private Integer isSignName;
	//签名
	private String signName;
	
	
	
	public Integer getIsSignName() {
		return isSignName;
	}
	public void setIsSignName(Integer isSignName) {
		this.isSignName = isSignName;
	}
	public String getSignName() {
		return signName;
	}
	public void setSignName(String signName) {
		this.signName = signName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBuscode() {
		return buscode;
	}
	public void setBuscode(String buscode) {
		this.buscode = buscode;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Timestamp getSendTime() {
		return sendTime;
	}
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}
	public Integer getIsAddName() {
		return isAddName;
	}
	public void setIsAddName(Integer isAddName) {
		this.isAddName = isAddName;
	}
	public String getAddName() {
		return addName;
	}
	public void setAddName(String addName) {
		this.addName = addName;
	}
	public String getSpUser() {
		return spUser;
	}
	public void setSpUser(String spUser) {
		this.spUser = spUser;
	}
	public Integer getIsUse() {
		return isUse;
	}
	public void setIsUse(Integer isUse) {
		this.isUse = isUse;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	
	
	
}