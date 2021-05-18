package com.montnets.emp.common.constant;
/**
 * 登录信息
 * @author Administrator
 *
 */
public class LoginInfo {
	private String sessionId;
	//操作员ID
	private Long userId;
	//企业编码
	private String corpCode;
	//用户登录名
	private String userName;
	//用户编码
	private String userCode;
	//用户guid
	private Long guId;
    //登录IP
    private String loginIP;
	
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public Long getGuId() {
		return guId;
	}
	public void setGuId(Long guId) {
		this.guId = guId;
	}

    public String getLoginIP() {
        return loginIP;
    }

    public void setLoginIP(String loginIP) {
        this.loginIP = loginIP;
    }

    //获取操作员信息字符串
	public String toString()
	{
		String loginparams = "";
		loginparams += "userid:"+this.userId.toString()+",";
		loginparams += "usercode:"+this.userCode+",";
		loginparams += "guid:"+this.guId.toString()+",";
		loginparams += "corpcode:"+this.corpCode+",";
		loginparams += "username:"+this.userName+",";
        loginparams += "loginip:"+this.loginIP+",";
		return loginparams;
	}
	
	//比较sessionId方法
	public boolean checkSessionId(String sessionId)
	{
		if(this.sessionId == null)
		{
			return false;
		}
		else
		{
			return this.sessionId.equals(sessionId);
		}
	}
}
