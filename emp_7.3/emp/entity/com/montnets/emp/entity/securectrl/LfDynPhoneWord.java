package com.montnets.emp.entity.securectrl;

import java.sql.Timestamp;

public class LfDynPhoneWord implements java.io.Serializable
{
	private static final long serialVersionUID = -453720090586239004L;
	//用户userid（外键）
	private Long userId;
	//四位动态码
	private String phoneWord;  
	//创建时间
	private Timestamp createTime; 
	//当天是否是第一次登录(1：已登录,0：未登录)
	private Integer isLogin;  
	//发送动态口令，网关返回msgid
	private String ptMsgId; 
	//网关是否成功发送，即是否状态报告返回(000000:成功)
	private String errorCode;  
	
	public String getPtMsgId() {
		return ptMsgId;
	}
	public void setPtMsgId(String ptMsgId) {
		this.ptMsgId = ptMsgId;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String getPhoneWord()
	{
		return this.phoneWord;
	}
	public void setPhoneWord(String phoneword)
	{
		this.phoneWord = phoneword;
	}
	
	public Timestamp getCreateTime()
	{
		return this.createTime;
	}
	public void setCreateTime(Timestamp createtime)
	{
		this.createTime = createtime;
	}
	
	public Integer getIsLogin()
	{
		return this.isLogin;
	}
	public void setIsLogin(Integer islogin)
	{
		this.isLogin = islogin;
	}
	
}
