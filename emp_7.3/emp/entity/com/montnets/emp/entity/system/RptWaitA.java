/**
 * 
 */
package com.montnets.emp.entity.system;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-27 下午02:39:08
 * @description
 */

public class RptWaitA implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5252602012231928014L;

	private Long id;

	private Integer ecId;

	private Integer userUid;

	private Integer LoginUid;

	private String userId;

	private Long ptMsgId;

	private String spnumber;

	private String phone;

	private String submitTime;

	private String doneTime;

	private String errorCode;
	
	private Long userMsgId;
	
	private Integer moduleId;

	public RptWaitA()
	{
		this.ecId = 0;
		this.userUid = 0;
		this.LoginUid = 0;
		this.userId = " ";
		this.ptMsgId = new Long(0);
		this.spnumber = " ";
		this.phone = " ";
		this.submitTime = " ";
		this.doneTime = " ";
		this.errorCode = " ";
	}

	
	public Long getUserMsgId() {
		return userMsgId;
	}

	public void setUserMsgId(Long userMsgId) {
		this.userMsgId = userMsgId;
	}

	public Integer getModuleId() {
		return moduleId;
	}

	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Integer getEcId()
	{
		return ecId;
	}

	public void setEcId(Integer ecId)
	{
		this.ecId = ecId;
	}

	public Integer getUserUid()
	{
		return userUid;
	}

	public void setUserUid(Integer userUid)
	{
		this.userUid = userUid;
	}

	public Integer getLoginUid()
	{
		return LoginUid;
	}

	public void setLoginUid(Integer loginUid)
	{
		LoginUid = loginUid;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public Long getPtMsgId()
	{
		return ptMsgId;
	}

	public void setPtMsgId(Long ptMsgId)
	{
		this.ptMsgId = ptMsgId;
	}

	public String getSpnumber()
	{
		return spnumber;
	}

	public void setSpnumber(String spnumber)
	{
		this.spnumber = spnumber;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getSubmitTime()
	{
		return submitTime;
	}

	public void setSubmitTime(String submitTime)
	{
		this.submitTime = submitTime;
	}

	public String getDoneTime()
	{
		return doneTime;
	}

	public void setDoneTime(String doneTime)
	{
		this.doneTime = doneTime;
	}

	public String getErrorCode()
	{
		return errorCode;
	}

	public void setErrorCode(String errorCode)
	{
		this.errorCode = errorCode;
	}

 
}
